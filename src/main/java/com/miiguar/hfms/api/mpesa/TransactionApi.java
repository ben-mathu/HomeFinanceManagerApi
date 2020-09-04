package com.miiguar.hfms.api.mpesa;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.api.mpesa.threadrunner.SendTransactionRunnable;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.data.budget.BudgetDao;
import com.miiguar.hfms.data.daraja.LnmoErrorResponse;
import com.miiguar.hfms.data.daraja.LnmoRequest;
import com.miiguar.hfms.data.daraja.LnmoResponse;
import com.miiguar.hfms.data.income.IncomeDao;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.jar.MoneyJarsDao;
import com.miiguar.hfms.data.jar.model.MoneyJar;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.tablerelationships.userhouse.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.miiguar.hfms.data.transactions.TransactionDao;
import com.miiguar.hfms.data.transactions.TransactionDto;
import com.miiguar.hfms.data.transactions.model.Transaction;
import com.miiguar.hfms.data.utils.DbEnvironment;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.BASE_URL;
import static com.miiguar.hfms.data.utils.URL.LNMO;
import static com.miiguar.hfms.data.utils.URL.LNMO_CALLBACK_URL;
import static com.miiguar.hfms.data.utils.URL.TRANSACTIONS;
import com.miiguar.hfms.utils.BufferRequestReader;
import static com.miiguar.hfms.utils.Constants.ACCESS_TOKEN;
import static com.miiguar.hfms.utils.Constants.DARAJA_DATE_FORMAT;
import static com.miiguar.hfms.utils.Constants.DATE_FORMAT;
import static com.miiguar.hfms.utils.Constants.PAY_BILL;
import com.miiguar.hfms.utils.GenerateCipher;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@WebServlet(name = "TransactionApi", urlPatterns = {API + TRANSACTIONS})
public class TransactionApi extends BaseServlet {
    private final TransactionDao transactionDao;
    private final MoneyJarsDao moneyJarsDao;
    private final BudgetDao budgetDao;
    private final UserHouseholdDao userHouseholdDao;
    private final IncomeDao incomeDao;
    
    public TransactionApi() {
        transactionDao = new TransactionDao();
        moneyJarsDao = new MoneyJarsDao();
        budgetDao = new BudgetDao();
        userHouseholdDao = new UserHouseholdDao();
        incomeDao = new IncomeDao();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter(DbEnvironment.USER_ID);
        
        TransactionDto dto = new TransactionDto();
        dto.setTransactions(transactionDao.getAllByUserId(userId));
        
        if (dto.getTransactions() != null) {
            writer = response.getWriter();
            writer.write(gson.toJson(dto));
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(httpServletRequest);

        LnmoRequest request = gson.fromJson(requestStr, LnmoRequest.class);

        MoneyJar jar = moneyJarsDao.get(request.getJarId());

        UserHouseholdRel rel = userHouseholdDao.get(request.getUserId());

        SimpleDateFormat sf = new SimpleDateFormat(DARAJA_DATE_FORMAT);
        Date now = new Date();
        String timestamp = sf.format(now);

        ConfigureApp app = new ConfigureApp();
        Properties properties = app.getProperties();

        String passKey = properties.getProperty("daraja.lnmo.passkey");

        GenerateCipher generateCipher = new GenerateCipher();
        String password = generateCipher.encode(request.getBusinessShortCode() + passKey + timestamp);

        request.setPassword(password);
        request.setTimestamp(timestamp);
        request.setTransactionType(PAY_BILL);
        request.setCallbackUrl(BASE_URL + LNMO_CALLBACK_URL);

//        Budget budget = new Budget();
//        budget.setCreatedAt(new SimpleDateFormat(DATE_FORMAT).format(new Date()));
//        budget.setHouseholdId(rel.getHouseId());
//        budget.setBudgetDesc("Top Up");
//        budget.setBudgetAmount(request.getAmount());
//        budget.setBudgetId(rand.nextString());

        // create transaction id
        GenerateRandomString rand = new GenerateRandomString(12);
        String tId = rand.nextString();
        
        // Start server to listen for mpesa callbacks for this request
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new SendTransactionRunnable(jar, tId));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error: Thread interrupted.", e);
        }

        Log.d(TAG, httpServletRequest.getContextPath() + "\n\tRequest body" + request.toString());

        InitUrlConnection<LnmoRequest> connection = new InitUrlConnection<>();
        BufferedReader streamReader = connection.getReaderForDarajaApi(
                request, LNMO, properties.getProperty(ACCESS_TOKEN), "POST"
        );

        String line = "";
        StringBuilder builder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {
            builder.append(line);
        }

        Log.d(TAG, builder.toString());

        LnmoResponse response = gson.fromJson(builder.toString(), LnmoResponse.class);

        if ("0".equals(response.getRespCode())) {
            
            Income income = incomeDao.get(request.getUserId());
            income.setAmount(income.getAmount() - jar.getTotalAmount());

            Transaction transaction = new Transaction();
            transaction.setId(tId);
            transaction.setTransactionDesc(jar.getName());
            transaction.setPaymentDetails(response.getCustomerMessage());
            transaction.setAmount(jar.getTotalAmount());
            transaction.setUserId(request.getUserId());
            transaction.setCreatedAt(new SimpleDateFormat(DATE_FORMAT).format(new Date().getTime()));
            transaction.setPaymentStatus(true);
            transaction.setPaymentTimestamp(new SimpleDateFormat(DATE_FORMAT).format(new Date().getTime()));

            int affected = transactionDao.save(transaction);

            if (!jar.isJarStatus()) {
                jar.setJarStatus(true);
            }
            
            jar.setPaymentStatus(true);
            moneyJarsDao.update(jar);
            sendNotification(affected, httpServletResponse, response);
        } else {
            LnmoErrorResponse error = gson.fromJson(builder.toString(), LnmoErrorResponse.class);
            Log.d(TAG, "Error/" + error.getErrorMessage() + ": " + error.getErrorMessage());

            sendErrorNotification(httpServletResponse);
        }
    }

    private void sendErrorNotification(HttpServletResponse resp) throws IOException {
        Report report = new Report();
        report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        report.setMessage("Transaction was not successful, please contact administrator.");

        writer = resp.getWriter();
        writer.write(gson.toJson(report));
    }

    private void sendNotification(int affected, HttpServletResponse httpServletResponse, LnmoResponse response) throws IOException {
        if (affected > 0) {
            Report report = new Report();
            report.setMessage(response.getCustomerMessage());
            report.setStatus(HttpServletResponse.SC_ACCEPTED);

            writer = httpServletResponse.getWriter();
            writer.write(gson.toJson(report));
        } else {
            // TODO: Handle SQL errors
        }
    }
}

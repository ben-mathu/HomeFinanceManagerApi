package com.benardmathu.hfms.api.transaction;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.api.transaction.threadrunner.SendTransactionRunnable;
import com.benardmathu.hfms.config.ConfigureApp;
import com.benardmathu.hfms.data.budget.BudgetService;
import com.benardmathu.hfms.data.daraja.LnmoErrorResponse;
import com.benardmathu.hfms.data.daraja.LnmoRequest;
import com.benardmathu.hfms.data.daraja.LnmoResponse;
import com.benardmathu.hfms.data.income.IncomeService;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.jar.MoneyJarsService;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleServices;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdService;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.transactions.TransactionService;
import com.benardmathu.hfms.data.transactions.TransactionDto;
import com.benardmathu.hfms.data.transactions.model.Transaction;
import com.benardmathu.hfms.data.utils.DbEnvironment;

import static com.benardmathu.hfms.data.utils.URL.BASE_URL;
import static com.benardmathu.hfms.data.utils.URL.LNMO;
import static com.benardmathu.hfms.data.utils.URL.LNMO_CALLBACK_URL;
import static com.benardmathu.hfms.data.utils.URL.TRANSACTIONS;
import com.benardmathu.hfms.utils.BufferRequestReader;
import static com.benardmathu.hfms.utils.Constants.ACCESS_TOKEN;
import static com.benardmathu.hfms.utils.Constants.DARAJA_DATE_FORMAT;
import static com.benardmathu.hfms.utils.Constants.DATE_FORMAT;
import static com.benardmathu.hfms.utils.Constants.PAY_BILL;
import com.benardmathu.hfms.utils.GenerateCipher;
import com.benardmathu.hfms.utils.GenerateRandomString;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@RestController
@RequestMapping(name = "TransactionApi", value = TRANSACTIONS)
public class TransactionApi extends BaseController {
    private final TransactionService transactionService;
    private final MoneyJarsService moneyJarsService;

    @Autowired
    private BudgetService budgetService;

    private final UserHouseholdService userHouseholdService;
    private final IncomeService incomeService;
    private final MoneyJarScheduleServices moneyJarScheduleServices;
    
    public TransactionApi() {
        transactionService = new TransactionService();
        moneyJarsService = new MoneyJarsService();
        userHouseholdService = new UserHouseholdService();
        incomeService = new IncomeService();
        moneyJarScheduleServices = new MoneyJarScheduleServices();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @GetMapping
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter(DbEnvironment.USER_ID);
        
        TransactionDto dto = new TransactionDto();
        dto.setTransactions(transactionService.getAllByUserId(Long.parseLong(userId)));
        
//        List<JarScheduleDateRel> paid = moneyJarScheduleDao.getAllPaid(householdId);
        
        if (dto.getTransactions() != null) {
            writer = response.getWriter();
            writer.write(gson.toJson(dto));
        }
    }

    @PostMapping
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(httpServletRequest);

        LnmoRequest request = gson.fromJson(requestStr, LnmoRequest.class);
        
        JarScheduleDateRel jarScheduleDateRel = moneyJarScheduleServices.get(request.getJarId());

        MoneyJar jar = moneyJarsService.get(jarScheduleDateRel.getJarId());

        UserHouseholdRel rel = userHouseholdService.get(request.getUserId());

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

        
        
        Income income = incomeService.get(request.getUserId());
        income.setAmount(income.getAmount() - jar.getTotalAmount());

        incomeService.update(income);
        
        Transaction transaction = new Transaction();
        transaction.setTransactionDesc(jar.getName());
        transaction.setPaymentDetails(gson.toJson(jar));
        transaction.setAmount(jar.getTotalAmount());
        transaction.setUserId(request.getUserId());
        transaction.setCreatedAt(new SimpleDateFormat(DATE_FORMAT).format(new Date().getTime()));
        transaction.setPaymentStatus(true);
        transaction.setPaymentTimestamp(new SimpleDateFormat(DATE_FORMAT).format(new Date().getTime()));

        transaction = transactionService.save(transaction);

        if (!jarScheduleDateRel.isJarStatus()) {
            jarScheduleDateRel.setJarStatus(true);
        }

        jarScheduleDateRel.setPaymentStatus(true);
        moneyJarScheduleServices.update(jarScheduleDateRel);
        sendNotification(transaction, httpServletResponse);
        
        // Start server to listen for mpesa callbacks for this request
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new SendTransactionRunnable(jar, transaction.getId()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error: Thread interrupted.", e);
        }

        Log.d(TAG, httpServletRequest.getContextPath() + "\n\tRequest body" + request);

        // sends an mpesa request if a business number has been specified
        if (request.getBusinessShortCode().isEmpty()) {
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

            } else {
                LnmoErrorResponse error = gson.fromJson(builder.toString(), LnmoErrorResponse.class);
                Log.d(TAG, "Error/" + error.getErrorMessage() + ": " + error.getErrorMessage());

                sendErrorNotification(httpServletResponse);
            }
        }
    }

    private void sendErrorNotification(HttpServletResponse resp) throws IOException {
        Report report = new Report();
        report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        report.setMessage("Transaction was not successful, please contact administrator.");

        writer = resp.getWriter();
        writer.write(gson.toJson(report));
    }

    private void sendNotification(Transaction transaction, HttpServletResponse httpServletResponse) throws IOException {
        if (transaction != null) {
            Report report = new Report();
            report.setMessage("Transaction updated successfully.");
            report.setStatus(HttpServletResponse.SC_ACCEPTED);

            writer = httpServletResponse.getWriter();
            writer.write(gson.toJson(report));
        } else {
            Report report = new Report();
            report.setMessage("Transaction not successfully.");
            report.setStatus(HttpServletResponse.SC_ACCEPTED);

            writer = httpServletResponse.getWriter();
            writer.write(gson.toJson(report));
        }
    }
}

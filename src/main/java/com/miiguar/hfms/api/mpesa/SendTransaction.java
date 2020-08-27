package com.miiguar.hfms.api.mpesa;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.api.mpesa.threadrunner.SendTransactionRunnable;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.data.budget.BudgetDao;
import com.miiguar.hfms.data.budget.model.Budget;
import com.miiguar.hfms.data.daraja.LnmoErrorResponse;
import com.miiguar.hfms.data.daraja.LnmoRequest;
import com.miiguar.hfms.data.daraja.LnmoResponse;
import com.miiguar.hfms.data.daraja.TransactionDao;
import com.miiguar.hfms.data.daraja.models.Transaction;
import com.miiguar.hfms.data.jar.MoneyJarsDao;
import com.miiguar.hfms.data.jar.model.MoneyJar;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.miiguar.hfms.data.utils.URL.*;
import static com.miiguar.hfms.utils.Constants.*;
import java.security.SecureRandom;

/**
 * @author bernard
 */
@WebServlet(API + TRANSACTIONS)
public class SendTransaction extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private TransactionDao transactionDao;
    private MoneyJarsDao moneyJarsDao;
    private BudgetDao budgetDao;
    private UserHouseholdDao userHouseholdDao;

    public SendTransaction() {
        transactionDao = new TransactionDao();
        moneyJarsDao = new MoneyJarsDao();
        budgetDao = new BudgetDao();
        userHouseholdDao = new UserHouseholdDao();
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

            Transaction transaction = new Transaction();
            transaction.setId(tId);
            transaction.setTransactionDesc(jar.getName());
            transaction.setPaymentDetails(response.getCustomerMessage());
            transaction.setAmount(jar.getTotalAmount());
            transaction.setJarId(jar.getMoneyJarId());
            transaction.setCreatedAt(new SimpleDateFormat(DATE_FORMAT).format(new Date().getTime()));

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

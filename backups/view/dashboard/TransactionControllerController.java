package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.daraja.LnmoRequest;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.utils.Log;
import com.benardmathu.hfms.view.base.BaseController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.benardmathu.hfms.data.utils.DbEnvironment.MONEY_JAR_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.GET_TRANSACTIONS;
import static com.benardmathu.hfms.data.utils.URL.SEND_TRANSACTION;
import static com.benardmathu.hfms.utils.Constants.TOKEN;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author bernard
 */
@Controller("/dashboard/transactions")
public class TransactionControllerController extends BaseController {
    private static final long serialVersionUID = 1L;

    public static final String TAG = TransactionControllerController.class.getSimpleName();

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(DbEnvironment.USER_ID);
        String token = req.getParameter(TOKEN);
        
        String requestParam = "?" + DbEnvironment.USER_ID + "=" + userId;
        
        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(URL.GET_TRANSACTIONS + requestParam, token, "GET");
        
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {            
            builder.append(line);
        }
        
        writer = resp.getWriter();
        writer.write(builder.toString());
    }

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter(TOKEN);
        String userId = req.getParameter(DbEnvironment.USER_ID);

        String shortCode = req.getParameter(SHORT_CODE);
        String amount = req.getParameter(AMOUNT);
        String payer = req.getParameter(PARTY_A);
        String payee = req.getParameter(PARTY_B);
        String phoneNumber = req.getParameter(PHONE_NUMBER);
        String accountRef = req.getParameter(ACCOUNT_REF);
        String transactionDesc = req.getParameter(TRANSACTION_DESC);
        String jarId = req.getParameter(DbEnvironment.MONEY_JAR_ID);

        LnmoRequest request = new LnmoRequest();
        request.setBusinessShortCode(shortCode);
        request.setAmount(amount);
        request.setPayer(payer);
        request.setPayee(payee);
        request.setPhoneNumber(phoneNumber);
        request.setAccountRef(accountRef);
        request.setTransactionDesc(transactionDesc);
        request.setJarId(Long.parseLong(jarId));
        request.setUserId(Long.parseLong(userId));

        Log.d(TAG, "Request body" + request.toString());

        InitUrlConnection<LnmoRequest> connection = new InitUrlConnection<>();
        BufferedReader streamReader = connection.getReader(
                request, URL.SEND_TRANSACTION, token, "POST"
        );

        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {
            builder.append(line);
        }

        Log.d(TAG, "Response body" + builder.toString());
        
        Report report = new Report();
        report.setMessage("This is important");
        report.setStatus(200);
        
        resp.setStatus(200);
//        report = gson.fromJson(builder.toString(), Report.class);
        
        if (report.getStatus() > 400) {
            resp.setStatus(HttpServletResponse.SC_OK);
            
            writer = resp.getWriter();
            writer.write(gson.toJson(report));
        } else {
            resp.setStatus(report.getStatus());

            writer = resp.getWriter();
            writer.write(builder.toString());
        }
    }
}

package com.miiguar.hfms.view.dashboard;

import com.miiguar.hfms.data.daraja.LnmoRequest;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.miiguar.hfms.data.utils.DbEnvironment.MONEY_JAR_ID;
import static com.miiguar.hfms.data.utils.DbEnvironment.USER_ID;
import static com.miiguar.hfms.data.utils.URL.GET_TRANSACTIONS;
import static com.miiguar.hfms.data.utils.URL.SEND_TRANSACTION;
import static com.miiguar.hfms.utils.Constants.LnmoRequestFields.*;
import static com.miiguar.hfms.utils.Constants.TOKEN;

/**
 * @author bernard
 */
@WebServlet("/dashboard/transactions/*")
public class TransactionServletController extends BaseServlet {
    private static final long serialVersionUID = 1L;

    public static final String TAG = TransactionServletController.class.getSimpleName();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        String token = req.getParameter(TOKEN);
        
        String requestParam = "?" + USER_ID + "=" + userId;
        
        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(GET_TRANSACTIONS + requestParam, token, "GET");
        
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {            
            builder.append(line);
        }
        
        writer = resp.getWriter();
        writer.write(builder.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String token = req.getParameter(TOKEN);
        String userId = req.getParameter(USER_ID);

        String shortCode = req.getParameter(SHORT_CODE);
        String amount = req.getParameter(AMOUNT);
        String payer = req.getParameter(PARTY_A);
        String payee = req.getParameter(PARTY_B);
        String phoneNumber = req.getParameter(PHONE_NUMBER);
        String accountRef = req.getParameter(ACCOUNT_REF);
        String transactionDesc = req.getParameter(TRANSACTION_DESC);
        String jarId = req.getParameter(MONEY_JAR_ID);

        LnmoRequest request = new LnmoRequest();
        request.setBusinessShortCode(shortCode);
        request.setAmount(amount);
        request.setPayer(payer);
        request.setPayee(payee);
        request.setPhoneNumber(phoneNumber);
        request.setAccountRef(accountRef);
        request.setTransactionDesc(transactionDesc);
        request.setJarId(jarId);
        request.setUserId(userId);

        Log.d(TAG, "Request body" + request.toString());

        InitUrlConnection<LnmoRequest> connection = new InitUrlConnection<>();
        BufferedReader streamReader = connection.getReader(
                request, SEND_TRANSACTION, token, "POST"
        );

        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {
            builder.append(line);
        }

        Log.d(TAG, "Response body" + builder.toString());

        Report report = gson.fromJson(builder.toString(), Report.class);
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
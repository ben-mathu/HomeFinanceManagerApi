package com.miiguar.hfms.view.dashboard;

import com.miiguar.hfms.data.daraja.LnmoRequest;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.miiguar.hfms.data.utils.DbEnvironment.USER_ID;
import static com.miiguar.hfms.data.utils.URL.SEND_TRANSACTION;
import static com.miiguar.hfms.utils.Constants.LnmoRequestFields.*;
import static com.miiguar.hfms.utils.Constants.TOKEN;

/**
 * @author bernard
 */
@WebServlet("/dashboard/transactions/send-payment")
public class TransactionServletController extends BaseServlet {
    private static final long serialVersionUID = 1L;

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

        LnmoRequest request = new LnmoRequest();
        request.setBusinessShortCode(shortCode);
        request.setAmount(amount);
        request.setPayer(payer);
        request.setPayee(payee);
        request.setPhoneNumber(phoneNumber);
        request.setAccountRef(accountRef);
        request.setTransactionDesc(transactionDesc);

        InitUrlConnection<LnmoRequest> connection = new InitUrlConnection<>();
        BufferedReader streamReader = connection.getReader(
                request, SEND_TRANSACTION, token, "POST"
        );

        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {
            builder.append(line);
        }

        Report report = gson.fromJson(builder.toString(), Report.class);
        if (200 == report.getStatus()) {
            // start mpesa callback listener
        } else {
            resp.setStatus(report.getStatus());

            writer = resp.getWriter();
            writer.write(builder.toString());
        }
    }
}

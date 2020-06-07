package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.models.user.Identification;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.Log;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.GENERATE_CODE;

/**
 * @author bernard
 */
@WebServlet(API + GENERATE_CODE)
public class GenerateConfirmationCode extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String requestStr = BufferRequest.bufferRequest(req);
        Identification user = gson.fromJson(requestStr, Identification.class);

        String code = generateCode();

        try {
            saveCode(code, user.getUser());
            sendCodeToEmail(user.getUser(), code);

            Report report = new Report();
            report.setMessage("Success");
            report.setStatus(HttpServletResponse.SC_OK);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        } catch (SQLException | MessagingException e) {
            Report report = new Report();
            report.setMessage("Error occurred please send the code again.");
            report.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
            Log.e(TAG, "Error storing confirmation code.", e);
        }
    }
}
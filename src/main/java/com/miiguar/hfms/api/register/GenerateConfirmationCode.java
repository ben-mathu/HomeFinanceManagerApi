package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.models.user.Identification;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.utils.sender.EmailSession;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.miiguar.hfms.api.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.API;

/**
 * @author bernard
 */
@WebServlet(API + "/registration/generate-confirmation-code")
public class GenerateConfirmationCode extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = GenerateConfirmationCode.class.getSimpleName();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String requestStr = BufferRequest.bufferRequest(req);
        Identification user = gson.fromJson(requestStr, Identification.class);

        String code = generateCode();

        try {
            saveCode(code, user.getUser());
            EmailSession session = new EmailSession();
            session.sendEmail(
                    user.getUser().getEmail(),
                    "Email Confirmation Code",
                    "<div style=\"text-align: center;\">" +
                            "<h5>User this code to confirm your email</h5>" +
                            "<h2>" + code + "</h2>" +
                            "</div>"
            );

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

    private void saveCode(String code, User user) throws SQLException {
        connection = jdbcConnection.getConnection(
                user.getUsername() + "_db",
                user.getUsername(),
                user.getPassword());
        PreparedStatement insertCode = connection.prepareStatement(
                "INSERT INTO " + CODE_TB_NAME + "(" +
                        CODE + "," + USER_ID + ")" +
                        "VALUES (" + code + "," + user.getUserId() + ")" +
                        " ON CONFLICT (" + USER_ID + ") DO UPDATE" +
                        " SET " + CODE + "=?"
        );
        insertCode.executeUpdate();
    }

    private String generateCode() {
        GenerateRandomString rand = new GenerateRandomString(6, new Random(), "0123456789");
        return rand.nextString();
    }
}
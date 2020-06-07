package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.models.user.Identification;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.CONFIRM;

/**
 * @author bernard
 */
@WebServlet(API + CONFIRM)
public class Confirm extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String requestStr = BufferRequest.bufferRequest(req);

        Identification id = gson.fromJson(requestStr, Identification.class);

        try {
            if (isCodeCorrect(id)) {

                Report report = new Report();
                report.setStatus(HttpServletResponse.SC_OK);
                report.setMessage("Success");
                String responseStr = gson.toJson(report);
                writer = resp.getWriter();
                writer.write(responseStr);
            } else {
                Report report = new Report();
                report.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                report.setMessage("</br>Sorry that code is invalid</br> Please try again or send another");
                String responseStr = gson.toJson(report);
                writer = resp.getWriter();
                writer.write(responseStr);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error confirming code", e);
        }
    }

    private boolean isCodeCorrect(Identification id) throws SQLException {
        String username = id.getUser().getUsername();
        String password = id.getUser().getPassword();
        String dbName = username + "_db";
        connection = jdbcConnection.getConnection(dbName, username, password);

        PreparedStatement codeConfirm = connection.prepareStatement(
                "SELECT * FROM " + CODE_TB_NAME + " WHERE " + COL_USER_ID + "=?"
        );
        codeConfirm.setInt(1, id.getUser().getUserId());

        ResultSet resultSet = codeConfirm.executeQuery();
        String code = "";
        while (resultSet.next()) {
            int c = resultSet.getInt(COL_CODE);
            code = String.valueOf(c);
        }

        if (code.equals(id.getCode())) {
            updateTableCode(code);
            return true;
        }
        return false;
    }

    private void updateTableCode(String code) throws SQLException {
        PreparedStatement update = connection.prepareStatement(
                "UPDATE " + CODE_TB_NAME + " SET " + COL_EMAIL_CONFIRMED + "=? " + "WHERE " + COL_CODE + "=?"
        );
        update.setBoolean(1, true);
        update.setString(2, code);
        update.executeUpdate();
    }
}
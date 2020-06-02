package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.models.ConfirmationResponse;
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

import static com.miiguar.hfms.api.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.API;

/**
 * @author bernard
 */
@WebServlet(API + "/registration/confirm")
public class Confirm extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = Confirm.class.getSimpleName();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String requestStr = BufferRequest.bufferRequest(req);

        Identification id = gson.fromJson(requestStr, Identification.class);
        ConfirmationResponse response = new ConfirmationResponse();

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
                report.setMessage("The code provided is not correct.");
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
                "SELECT * FROM " + CODE_TB_NAME + " WHERE " + USER_ID + "=?"
        );
        codeConfirm.setInt(1, id.getUser().getUserId());

        ResultSet resultSet = codeConfirm.executeQuery();
        String code = "";
        while (resultSet.next()) {
            code = resultSet.getString(CODE);
        }

        if (code.equals(id.getCode())) {
            return true;
        }
        return false;
    }
}
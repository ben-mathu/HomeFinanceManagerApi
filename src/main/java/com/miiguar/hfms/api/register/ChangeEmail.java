package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.miiguar.hfms.data.utils.DbEnvironment.COL_EMAIL;
import static com.miiguar.hfms.data.utils.DbEnvironment.USERS_TB_NAME;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.CHANGE_EMAIL;

/**
 * @author bernard
 */
@WebServlet(API + CHANGE_EMAIL)
public class ChangeEmail extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String requestStr = BufferRequest.bufferRequest(req);

        User user = gson.fromJson(requestStr, User.class);

        Report report = new Report();

        try {
            changeEmail(user);
            report.setMessage("Success");
            report.setStatus(HttpServletResponse.SC_OK);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        } catch (SQLException throwables) {

            report.setMessage("Error changing email");
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
            Log.e(TAG, "Error changing email", throwables);
        }
    }

    private void changeEmail(User user) throws SQLException {
        String username = user.getUsername();
        String password = user.getPassword();
        String dbName = user.getPassword();

        connection = jdbcConnection.getConnection(dbName, username, password);

        PreparedStatement changeEmail = connection.prepareStatement(
                "UPDATE " + USERS_TB_NAME +
                        "SET " + COL_EMAIL + "=?"
        );
        changeEmail.setString(1, user.getEmail());
        changeEmail.executeUpdate();
    }
}
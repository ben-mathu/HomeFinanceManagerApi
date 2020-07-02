package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.CHANGE_EMAIL;

/**
 * @author bernard
 */
@WebServlet(API + CHANGE_EMAIL)
public class ChangeEmail extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private Properties prop;
    private JdbcConnection jdbcConnection;
    private Connection connection;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        User user = gson.fromJson(requestStr, User.class);

        Report report = new Report();

        ConfigureDb db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();

        try {
            connection = jdbcConnection.getConnection(prop.getProperty("db.main_db"));

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
        } finally {
            try {
                closeConnection();
            } catch (SQLException throwables) {
                Log.e(TAG, "An error occurred while closing connection", throwables);
            }
        }
    }

    private void changeEmail(User user) throws SQLException {

    }

    @Override
    public void closeConnection() throws SQLException {
        if (!connection.isClosed())
            connection.close();
        connection = null;
        jdbcConnection.disconnect();
        jdbcConnection = null;
    }
}
package com.miiguar.hfms.api.login;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.config.EmailEnv;
import com.miiguar.hfms.data.models.user.UserRequest;
import com.miiguar.hfms.data.models.user.UserResponse;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.LOGIN;
import static com.miiguar.hfms.utils.Constants.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet(API + LOGIN)
public class Login extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonRequest = BufferRequest.bufferRequest(request);

        PrintWriter writer;
        Report report = null;

        UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);
        User user = userRequest.getUser();
        String username = user.getUsername();
        String password = user.getPassword();

        response.setContentType(APPLICATION_JSON);
        try {
            // initialize connection
            connection = jdbcConnection.getConnection(
                    user.getUsername() + "_db",
                    user.getUsername(),
                    user.getUsername()
            );

            // validate credentials
            if (isCredentialsValid(username, password)) {
                JwtTokenUtil tokenUtil = new JwtTokenUtil();
                Calendar calendar = Calendar.getInstance();
                Date date = new Date(calendar.getTimeInMillis() + TimeUnit.DAYS.toMillis(2));

                GenerateRandomString randomString = new GenerateRandomString(12);

                ConfigureApp app = new ConfigureApp();
                Properties prop = app.getProperties();
                String subject = prop.getProperty(SUBJECT, "");
                String token = tokenUtil.generateToken(ISSUER, subject, date);

                report = new Report();
                report.setMessage("Success");
                report.setStatus(HttpServletResponse.SC_OK);
                report.setSubject(subject);
                report.setToken(token);

                String jsonResp = gson.toJson(report);
                response.setStatus(HttpServletResponse.SC_OK);
                writer = response.getWriter();
                writer.print(jsonResp);
            } else {

                report = new Report();
                report.setStatus(HttpServletResponse.SC_FORBIDDEN);
                report.setMessage("Invalid entry for username/password");

                String jsonResp = gson.toJson(report);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                writer = response.getWriter();
                writer.print(jsonResp);
            }
        } catch (SQLException throwables) {
            Log.e(TAG, "Error: logging in the user.", throwables);

            EmailEnv emailEnv = new EmailEnv();
            Properties prop = emailEnv.getProperties();
            String email = prop.getProperty("email");

            if (throwables.getMessage().contains(user.getUsername())) {
                report = new Report();
                report.setStatus(HttpServletResponse.SC_FORBIDDEN);
                report.setMessage("Invalid Username/password.");
            } else {
                report = new Report();
                report.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                report.setMessage("An Error occurred,</br> please contact the admin: " + email);
            }

            UserResponse res = new UserResponse();
            res.setReport(report);
            String jsonResp = gson.toJson(res);
            writer = response.getWriter();
            writer.print(jsonResp);
        }
    }

    private boolean isCredentialsValid(String username, String password) throws SQLException {
        if (username.isEmpty() || password.isEmpty()) return false;

        PreparedStatement query = connection.prepareStatement(
                "SELECT * FROM " + USERS_TB_NAME + " WHERE " + COL_USERNAME + "=?"
        );
        query.setString(1, username);
        ResultSet result = query.executeQuery();

        while (result.next()) {
            if (!username.equals(result.getString(COL_USERNAME)) && !password.equals(result.getString(COL_PASSWORD))) {
                return false;
            }
        }

        return true;
    }
}
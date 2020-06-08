package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureApp;
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
import static com.miiguar.hfms.data.utils.URL.REGISTRATION;
import static com.miiguar.hfms.utils.Constants.ISSUER;
import static com.miiguar.hfms.utils.Constants.SUBJECT;

/**
 * @author bernard
 */
@WebServlet(API + REGISTRATION)
public class Register extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String jsonRequest = BufferRequest.bufferRequest(req);

        ConfigureApp configureApp = new ConfigureApp();
        Properties prop = configureApp.getProperties();

        UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);
        User user = userRequest.getUser();

        Report report = null;
        try {
            if (isUserDbCreated(user.getUsername())) {
                report = new Report();
                report.setMessage("username already in use");
                report.setStatus(HttpServletResponse.SC_FORBIDDEN);

                UserResponse response = new UserResponse();
                response.setReport(report);
                String jsonResp = gson.toJson(response);

                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                writer = resp.getWriter();
                writer.write(jsonResp);
            } else {

                // create a db
                createDb(user.getUsername(), user.getPassword());

                addUser(user);
                user = getUser();

                // generate a token
                JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
                Calendar calendar = Calendar.getInstance();
                Date date = new Date(calendar.getTimeInMillis() + TimeUnit.DAYS.toMillis(2));

                String subject = prop.getProperty(SUBJECT, "");
                String token = jwtTokenUtil.generateToken(ISSUER, subject, date);

                report = new Report();
                report.setMessage("Success");
                report.setStatus(HttpServletResponse.SC_OK);
                report.setToken(token);

                UserResponse response = new UserResponse();
                response.setReport(report);
                response.setUser(user);
                String jsonResp = gson.toJson(response);

                writer = resp.getWriter();
                writer.print(jsonResp);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error creating user", e);

            report = new Report();

            String msg = "";
            if (e.getMessage().contains(user.getUsername())) {
                report.setStatus(HttpServletResponse.SC_FORBIDDEN);
                msg = "User exists, please try another username";
            } else {
                String email = prop.getProperty("admin.email");
                msg = "An error has occurred, please contact the developer: " + email;
            }

            report.setMessage(msg);
            UserResponse response = new UserResponse();
            response.setReport(report);
            String jsonResp = gson.toJson(response);

            PrintWriter out = resp.getWriter();
            out.write(jsonResp);
        }
    }

    private void addUser(User user) throws SQLException {
        PreparedStatement insertSmt = connection.prepareStatement(
                "INSERT INTO " + USERS_TB_NAME + " (" +
                        COL_USERNAME + "," + COL_EMAIL + "," + COL_PASSWORD + "," + COL_IS_ADMIN + ") " +
                        "VALUES ('" +
                        user.getUsername() + "','" +
                        user.getEmail() + "','" +
                        user.getPassword() + "'," +
                        user.isAdmin() + ")"
        );
        insertSmt.executeUpdate();
    }

    private User getUser() throws SQLException {

        User user = new User();
        PreparedStatement getUserSmt = connection.prepareStatement(
                "SELECT * FROM " + USERS_TB_NAME
        );
        ResultSet result = getUserSmt.executeQuery();
        while (result.next()) {
            user.setUserId(result.getInt(COL_USER_ID));
            user.setUsername(result.getString(COL_USERNAME));
            user.setEmail(result.getString(COL_EMAIL));
            user.setPassword(result.getString(COL_PASSWORD));
            user.setAdmin(result.getBoolean(COL_IS_ADMIN));
        }
        return user;
    }
}
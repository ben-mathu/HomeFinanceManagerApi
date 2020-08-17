package com.miiguar.hfms.api.login;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.config.EmailEnv;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.user.UserDao;
import com.miiguar.hfms.data.user.UserRequest;
import com.miiguar.hfms.data.user.UserResponse;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.utils.DbEnvironment;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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

    private UserDao userDao = new UserDao();

    private ConfigureDb db = new ConfigureDb();
    private Properties prop = db.getProperties();
    private JdbcConnection jdbcConnection = new JdbcConnection();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonRequest = BufferRequestReader.bufferRequest(request);

        PrintWriter writer;
        Report report = null;

        UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);
        User user = userRequest.getUser();
        String username = user.getUsername();
        String password = user.getPassword();

        this.db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();
        response.setContentType(APPLICATION_JSON);

        // validate credentials
        User u = userDao.validateCredentials(username, password);
        if (u != null) {
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

            UserResponse resp = new UserResponse();
            resp.setReport(report);
            resp.setUser(u);

            String jsonResp = gson.toJson(resp);
            response.setStatus(HttpServletResponse.SC_OK);
            writer = response.getWriter();
            writer.write(jsonResp);
        } else {

            report = new Report();
            report.setStatus(HttpServletResponse.SC_FORBIDDEN);
            report.setMessage("Invalid entry for username/password");

            UserResponse obj = new UserResponse();
            obj.setReport(report);

            String jsonResp = gson.toJson(obj);
            writer = response.getWriter();
            writer.write(jsonResp);
        }
//        } catch (SQLException throwables) {
//            Log.e(TAG, "Error: logging in the user.", throwables);
//
//            EmailEnv emailEnv = new EmailEnv();
//            Properties prop = emailEnv.getProperties();
//            String email = prop.getProperty("email");
//
//            if (throwables.getMessage().contains(user.getUsername())) {
//                report = new Report();
//                report.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                report.setMessage("Invalid Username/password.");
//            } else {
//                report = new Report();
//                report.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                report.setMessage("An Error occurred,</br> please contact the admin: " + email);
//            }
//
//            UserResponse res = new UserResponse();
//            res.setReport(report);
//            String jsonResp = gson.toJson(res);
//            writer = response.getWriter();
//            writer.write(jsonResp);
//        }
    }
}
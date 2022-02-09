package com.benardmathu.hfms.api.login;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.config.ConfigureApp;
import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.data.user.UserDao;
import com.benardmathu.hfms.data.user.UserRepository;
import com.benardmathu.hfms.data.user.UserRequest;
import com.benardmathu.hfms.data.user.UserResponse;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.GenerateRandomString;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.benardmathu.hfms.data.utils.URL.LOGIN;
import static com.benardmathu.hfms.utils.Constants.*;
import com.benardmathu.hfms.utils.PasswordUtil;
import com.benardmathu.tokengeneration.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bernard
 */
@RestController
@RequestMapping(LOGIN)
public class Login extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private UserRepository userRepository;

    private UserDao userDao = new UserDao();

    private ConfigureDb db = new ConfigureDb();
    private Properties prop = db.getProperties();
    private JdbcConnection jdbcConnection = new JdbcConnection();

    @PostMapping
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
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // validate credentials
        User u = userDao.validateCredentials(username, password);
        if (u == null) {
            report = new Report();
            report.setStatus(HttpServletResponse.SC_FORBIDDEN);
            report.setMessage("Invalid entry for username/password");

            UserResponse obj = new UserResponse();
            obj.setReport(report);

            String jsonResp = gson.toJson(obj);
            writer = response.getWriter();
            writer.write(jsonResp);
        } else if (PasswordUtil.verifyPassword(password, u.getPassword(), u.getSalt())) {
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
    }
}
package com.miiguar.hfms.api.register;

import com.google.gson.Gson;
import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.models.user.UserRequest;
import com.miiguar.hfms.data.models.user.UserResponse;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.Log;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.miiguar.hfms.utils.Constants.API;
import static com.miiguar.hfms.utils.Constants.ISSUER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet(API + "/registration")
public class Register extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = Register.class.getSimpleName();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String jsonRequest = BufferRequest.bufferRequest(req);

        Gson gson = new Gson();
        UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);
        User user = userRequest.getUser();

        resp.setContentType(APPLICATION_JSON);
        PrintWriter writer;
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

                // generate a token
                JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
                long millis = TimeUnit.DAYS.toMillis(2);
                String token = jwtTokenUtil.generateToken(ISSUER, user.getUsername(), new Date(millis));
                report = new Report();
                report.setMessage("Success");
                report.setStatus(HttpServletResponse.SC_OK);
                report.setToken(token);
                UserResponse response = new UserResponse();
                response.setReport(report);
                String jsonResp = gson.toJson(response);

                writer = resp.getWriter();
                writer.print(jsonResp);
            }
        } catch (SQLException e) {
            logger.error(TAG, e);

            report = new Report();
            report.setStatus(HttpServletResponse.SC_FORBIDDEN);
            report.setMessage(e.getMessage());
            UserResponse response = new UserResponse();
            response.setReport(report);
            String jsonResp = gson.toJson(response);

            writer = resp.getWriter();
            writer.write(jsonResp);
        } catch (IOException e) {
            Log.e(TAG, "error: ", logger, e);
        }
    }

    /**
     * Create a db for individual users
     *
     * @param username
     * @param password
     * @throws SQLException
     */
    private void createDb(String username, String password) throws SQLException {
        ConfigureDb configureDb = new ConfigureDb();
        Properties prop = configureDb.readProperties();

        PreparedStatement createUser = connection.prepareStatement(
                "CREATE USER " + username + " ENCRYPTED PASSWORD '" + password + "'"
        );
        createUser.execute();

        String dbName = username + "_db";
        PreparedStatement statement = connection.prepareStatement("CREATE DATABASE " +
                dbName + " OWNER " + username + " TABLESPACE " + prop.getProperty("db.tablespace"));
        statement.execute();
        connection = jdbcConnection.getConnection(dbName, username, password);
    }
}
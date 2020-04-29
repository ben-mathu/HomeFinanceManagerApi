package com.miiguar.hfms.api.registration;

import com.google.gson.Gson;
import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.model.user.User;
import com.miiguar.hfms.data.status.MessageReport;
import com.miiguar.hfms.utils.BufferRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String jsonRequest = BufferRequest.bufferRequest(req);

        Gson gson = new Gson();
        User user = gson.fromJson(jsonRequest, User.class);

        resp.setContentType(APPLICATION_JSON);
        PrintWriter writer;
        try {
            MessageReport report = null;
            if (isUserDbCreated(user.getUsername())) {
                report = new MessageReport(HttpServletResponse.SC_FORBIDDEN,
                        "username already in use");

                String jsonResp = gson.toJson(report);
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                writer = resp.getWriter();
                writer.print(jsonResp);
            } else {

                createDb(user.getUsername());

                // TODO: Add user to database

                report = new MessageReport(HttpServletResponse.SC_OK, "Success");
                String jsonResp = gson.toJson(report);
                resp.setStatus(HttpServletResponse.SC_OK);
                writer = resp.getWriter();
                writer.print(jsonResp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDb(String username) throws SQLException {
        ConfigureDb configureDb = new ConfigureDb();
        Properties prop = configureDb.readProperties();

        String dbName = username + "_db";
        PreparedStatement statement = connection.prepareStatement("CREATE DATABASE " +
                dbName + " OWNER " + prop.getProperty("db.username") + " ");
        connection = jdbcConnection.getConnection(dbName);
    }
}
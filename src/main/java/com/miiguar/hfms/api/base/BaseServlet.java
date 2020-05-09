package com.miiguar.hfms.api.base;

import com.google.gson.Gson;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.HandleLogging;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bernard
 */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String TAG = this.getClass().getSimpleName();

    public JdbcConnection jdbcConnection = new JdbcConnection();
    public Logger logger = Logger.getRootLogger();

    public Connection connection = null;
    public Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandleLogging.handleLogging(req, logger, TAG);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/login")) {
            String jsonRequest = BufferRequest.bufferRequest(req);
            User user = gson.fromJson(jsonRequest, User.class);
            connection = jdbcConnection.getConnection(user.getUsername() + "_db");
        }

        HandleLogging.handleLogging(req, logger, TAG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandleLogging.handleLogging(req, logger, TAG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandleLogging.handleLogging(req, logger, TAG);
    }

    public boolean isUserDbCreated(String username) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet result = dbMetaData.getCatalogs();

        while(result.next()) {
            String dbName = result.getString(1).split("_")[0];
            if (dbName.equals(username)) {
                return true;
            }
        }
        return false;
    }
}
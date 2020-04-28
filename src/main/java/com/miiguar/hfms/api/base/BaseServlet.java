package com.miiguar.hfms.api.base;

import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.model.user.User;
import com.miiguar.hfms.utils.BufferRequest;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author bernard
 */
@WebServlet("/")
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String TAG = this.getClass().getSimpleName();

    private JdbcConnection jdbcConnection = new JdbcConnection();
    private ConfigureDb configureDb = new ConfigureDb();

    public Logger logger = Logger.getRootLogger();
    public Properties prop = configureDb.readProperties();
    public Connection connection = jdbcConnection.getConnection("",
            prop.getProperty("db.username"),
            prop.getProperty("db.password"),
            prop.getProperty("db.url"));
    public Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        handleLogging(throwable, req);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String uri = req.getRequestURI();

        if (uri.equals("/login")) {
            String jsonRequest = BufferRequest.bufferRequest(req);
            User user = gson.fromJson(jsonRequest, User.class);
            connection = jdbcConnection.getConnection(
                    user.getUsername() + "_db",
                    prop.getProperty("db.username"),
                    prop.getProperty("db.password"),
                    prop.getProperty("db.url")
            );
        }
        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        handleLogging(throwable, req);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        handleLogging(throwable, req);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        handleLogging(throwable, req);
    }

    private void handleLogging(Throwable throwable, HttpServletRequest req) {
        logger.setLevel(Level.DEBUG);
        if (throwable != null) {
            logger.error(TAG, throwable);
        } else {
            logger.log(Level.INFO, req.getRequestURI());
            logger.log(Level.INFO, req.getAuthType());
            logger.log(Level.INFO, req.getHeader("Authorization"));
        }
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
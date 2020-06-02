package com.miiguar.hfms.api.base;

import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.models.user.UserRequest;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.Log;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.miiguar.hfms.api.utils.Constants.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
    public PrintWriter writer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON);
        Log.handleLogging(req, TAG);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON);

        String uri = req.getRequestURI();
        if (uri.endsWith("/login")) {
            String jsonRequest = BufferRequest.bufferRequest(req);
            UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);
            User user = userRequest.getUser();
            connection = jdbcConnection.getConnection(
                    user.getUsername() + "_db",
                    user.getUsername(),
                    user.getPassword()
            );
        } else if (uri.endsWith("/registration")){
            // get database properties
            ConfigureDb dbProps = new ConfigureDb();
            Properties prop = dbProps.getProperties();
            String mainDb = prop.getProperty("db.main_db");
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            connection = jdbcConnection.getConnection(
                    mainDb,
                    username,
                    password
            );
        }

        Log.handleLogging(req, TAG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON);
        Log.handleLogging(req, TAG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON);
        Log.handleLogging(req, TAG);
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

    private void createAllTables() throws SQLException {
        PreparedStatement users = connection.prepareStatement(
                "CREATE TABLE " + USERS_TB_NAME + " ("+
                        USER_ID + " SERIAL,"+
                        USERNAME + " varchar(25) NOT NULL,"+
                        EMAIL + " text NOT NULL UNIQUE,"+
                        PASSWORD + " varchar(255) NOT NULL,"+
                        IS_ADMIN + " BOOLEAN NOT NULL," +
                        "CONSTRAINT " + PRIV_KEY_USERS + " PRIMARY KEY(" +
                        USER_ID + "," +
                        USERNAME + "," +
                        EMAIL + "))"
        );
        users.execute();

        // Create table for code
        PreparedStatement code = connection.prepareStatement(
                "CREATE TABLE " + CODE_TB_NAME + " ("+
                        CODE + " integer," +
                        USER_ID + " integer REFERENCES " + USERS_TB_NAME + "," +
                        "CONSTRAINT pk_username_email PRIMARY KEY(" +
                        USER_ID + "))"
        );
        code.execute();
    }

    /**
     * Create a db for individual users
     *
     * @param username
     * @param password
     * @throws SQLException
     */
    public void createDb(String username, String password) throws SQLException {
        ConfigureDb configureDb = new ConfigureDb();
        Properties prop = configureDb.getProperties();

        String dbName = username + "_db";

        // Create user/role
        PreparedStatement createUserSmt = connection.prepareStatement(
                "CREATE USER " + username + " ENCRYPTED PASSWORD '" + password + "'"
        );
        createUserSmt.execute();

        // that role create a db
        addDb(username, prop);

        // initialize the connection to the new database and db role
        connection = jdbcConnection.getConnection(dbName, username, password);
        createAllTables();
    }

    private void addDb(String username, Properties prop) throws SQLException {
        String dbName = username + "_db";
        PreparedStatement createDbSmt = connection.prepareStatement(
                "CREATE DATABASE " + dbName + " OWNER " + username + " TABLESPACE " + prop.getProperty("db.tablespace")
        );
        createDbSmt.execute();
    }
}
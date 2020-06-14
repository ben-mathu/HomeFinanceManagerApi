package com.miiguar.hfms.api.base;

import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.models.user.UserRequest;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.utils.BufferRequest;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.utils.sender.EmailSession;

import org.apache.log4j.Logger;

import javax.mail.MessagingException;
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
import java.util.Random;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.utils.Constants.USERNAME;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected final String TAG = this.getClass().getSimpleName();

    protected JdbcConnection jdbcConnection = new JdbcConnection();

    protected Connection connection = null;
    protected Gson gson = new Gson();
    protected PrintWriter writer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON);
        Log.handleLogging(req, TAG);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON);

        String uri = req.getRequestURI();
        if (uri.endsWith("/registration")){
            // get database properties
            ConfigureDb dbProps = new ConfigureDb();
            Properties prop = dbProps.getProperties();
            String mainDb = prop.getProperty("db.main_db");
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            try {
                connection = jdbcConnection.getConnection(
                        mainDb,
                        username,
                        password
                );
            } catch (SQLException throwables) {
                Log.e(TAG, "An error has occurred connecting to the db", throwables);
            }
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

    protected String generateCode() {
        GenerateRandomString rand = new GenerateRandomString(6, new Random(), "0123456789");
        return rand.nextString();
    }

    protected void sendCodeToEmail(User user, String code) throws MessagingException {
        EmailSession session = new EmailSession();
        session.sendEmail(
                user.getEmail(),
                "Email Confirmation Code",
                "<div style=\"text-align: center;\">" +
                        "<h5>User this code to confirm your email</h5>" +
                        "<h2>" + code + "</h2>" +
                        "</div>"
        );
    }

    protected void saveCode(String code, User user) throws SQLException {
        connection = jdbcConnection.getConnection(
                user.getUsername() + "_db",
                user.getUsername(),
                user.getUsername());
        PreparedStatement insertCode = connection.prepareStatement(
                "INSERT INTO " + CODE_TB_NAME + "(" +
                        COL_CODE + "," + COL_USER_ID + ")" +
                        "VALUES (?,?)" +
                        " ON CONFLICT (" + COL_USER_ID + ")" +
                        " DO UPDATE" +
                        " SET " + COL_CODE + "=?"+
                        " WHERE " + CODE_TB_NAME + "." + COL_USER_ID + "=?"
        );
        insertCode.setString(1, code);
        insertCode.setString(2, user.getUserId());
        insertCode.setString(3, code);
        insertCode.setString(4, user.getUserId());
        insertCode.executeUpdate();
    }

    protected boolean isUserDbCreated(String username) throws SQLException {
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
                        COL_USER_ID + " varchar(12),"+
                        COL_USERNAME + " varchar(25) NOT NULL UNIQUE,"+
                        COL_EMAIL + " text NOT NULL UNIQUE,"+
                        COL_PASSWORD + " varchar(255) NOT NULL,"+
                        COL_IS_ADMIN + " BOOLEAN NOT NULL," +
                        "CONSTRAINT " + PRIV_KEY_USERS + " PRIMARY KEY (" + COL_USER_ID + "))"
        );
        users.execute();

        // Create table for code
        PreparedStatement code = connection.prepareStatement(
                "CREATE TABLE " + CODE_TB_NAME + " ("+
                        COL_CODE + " text," +
                        COL_USER_ID + " varchar(12) UNIQUE," +
                        COL_EMAIL_CONFIRMED + " BOOLEAN DEFAULT false," +
                        "CONSTRAINT " + PRIV_KEY_CODE + " PRIMARY KEY (" + COL_CODE + ")," +
                        "CONSTRAINT " + FK_TB_CODE_USER_ID + " FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + USERS_TB_NAME + "(" + COL_USER_ID + "))"
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
    protected void createDb(String username, String password) throws SQLException {
        ConfigureDb configureDb = new ConfigureDb();
        Properties prop = configureDb.getProperties();

        String dbName = username + "_db";

        // Create user/role
        PreparedStatement createUserSmt = connection.prepareStatement(
                "CREATE USER " + username + " ENCRYPTED PASSWORD '" + username + "'"
        );
        createUserSmt.execute();

        // that role create a db
        addDb(username, prop);

        // initialize the connection to the new database and db role
        connection = jdbcConnection.getConnection(dbName, username, username);
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
package com.miiguar.hfms.api.base;

import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.code.CodeDao;
import com.miiguar.hfms.data.code.model.Code;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.user.UserDao;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.utils.sender.EmailSession;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected final String TAG = this.getClass().getSimpleName();

    protected Gson gson = new Gson();
    protected PrintWriter writer;

    abstract public void closeConnection() throws SQLException;
}
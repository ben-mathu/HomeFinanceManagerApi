package com.miiguar.hfms.view.base;

import com.google.gson.Gson;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.utils.HandleLogging;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

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
        HandleLogging.handleLogging(req, logger, TAG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandleLogging.handleLogging(req, logger, TAG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandleLogging.handleLogging(req, logger, TAG);
    }


}
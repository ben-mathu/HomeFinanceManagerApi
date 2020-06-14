package com.miiguar.hfms.view.base;

import com.google.gson.Gson;
import com.miiguar.hfms.utils.Log;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.miiguar.hfms.utils.Constants.TOKEN;
import static com.miiguar.hfms.utils.Constants.USER_ID;

/**
 * @author bernard
 */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String TAG = this.getClass().getSimpleName();

    public Logger logger = Logger.getRootLogger();
    public Gson gson = new Gson();
    public PrintWriter writer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Log.handleLogging(req, TAG);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Log.handleLogging(req, TAG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Log.handleLogging(req, TAG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Log.handleLogging(req, TAG);
    }

    protected String getTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    protected String getUserIdFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_ID.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}
package com.miiguar.hfms.view.login;

import com.miiguar.hfms.data.status.MessageReport;
import com.miiguar.hfms.view.base.BaseServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

        RequestDispatcher rd = req.getRequestDispatcher("views/login.jsp");
        rd.include(req, resp);
    }
}
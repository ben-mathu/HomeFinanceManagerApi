package com.miiguar.hfms.view.registration;

import com.miiguar.hfms.api.base.BaseServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bernard
 */
@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

        RequestDispatcher rd = req.getRequestDispatcher("views/registration.jsp");
        rd.include(req, resp);
    }
}
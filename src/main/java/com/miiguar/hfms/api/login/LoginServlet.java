package com.miiguar.hfms.api.login;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.model.user.User;
import com.miiguar.hfms.data.status.MessageReport;
import com.miiguar.hfms.utils.BufferRequest;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);

        response.setContentType(APPLICATION_JSON);
        PrintWriter writer;
        MessageReport report = null;

        if (connection == null) {
            report = new MessageReport(HttpServletResponse.SC_FORBIDDEN,
                    "Invalid entry for username/password");

            String jsonResp = gson.toJson(report);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writer = response.getWriter();
            writer.print(jsonResp);
        } else {

            // TODO: Check password

            report = new MessageReport(HttpServletResponse.SC_OK, "Success");
            String jsonResp = gson.toJson(report);
            response.setStatus(HttpServletResponse.SC_OK);
            writer = response.getWriter();
            writer.print(jsonResp);
        }
    }
}
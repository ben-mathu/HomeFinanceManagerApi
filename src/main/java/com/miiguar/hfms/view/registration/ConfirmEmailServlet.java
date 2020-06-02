package com.miiguar.hfms.view.registration;

import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.data.models.ConfirmationResponse;
import com.miiguar.hfms.data.models.user.Identification;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;
import com.miiguar.hfms.view.result.ErrorResults;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import static com.miiguar.hfms.api.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.TOKEN;

/**
 * @author bernard
 */
@WebServlet("/registration/email-confirmed")
public class ConfirmEmailServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        PrintWriter writer;

        String code = req.getParameter(CODE);
        String username = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);
        String email = req.getParameter(EMAIL);

        final ErrorResults results = new ErrorResults();
        if (code.length() != 6) {
            results.setCodeError("The code you provided is invalid.");
        } else {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            Identification id = new Identification();
            id.setCode(code);
            id.setUser(user);

            String token = (String) req.getSession().getAttribute(TOKEN);
            if (token == null) token = "";

            InitUrlConnection<Identification, Report> connection = new InitUrlConnection<>();
            Report item = connection.getReader(id, CONFIRM, token);
            if (item != null) {
                if (item.getStatus() != 200) {
                    String response = gson.toJson(item);

                    resp.setStatus(item.getStatus());
                    writer = resp.getWriter();
                    writer.write(response);
                } else {
                    writer = resp.getWriter();
                    String redirect = req.getContextPath() + "/dashboard";
                    writer.write(redirect);
                }
            }
            connection.close();
        }
    }
}
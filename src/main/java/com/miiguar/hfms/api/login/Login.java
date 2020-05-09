package com.miiguar.hfms.api.login;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.status.MessageReport;
import com.miiguar.hfms.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static com.miiguar.hfms.utils.Constants.API;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet(API + "/login")
public class Login extends BaseServlet {
    private static final long serialVersionUID = 1L;

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
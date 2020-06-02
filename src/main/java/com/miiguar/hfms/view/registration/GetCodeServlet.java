package com.miiguar.hfms.view.registration;

import com.miiguar.hfms.data.models.ConfirmationResponse;
import com.miiguar.hfms.data.models.user.Identification;
import com.miiguar.hfms.data.models.user.UserResponse;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.miiguar.hfms.api.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.API;
import static com.miiguar.hfms.utils.Constants.TOKEN;

/**
 * @author bernard
 */
@WebServlet(API + "/registration/get-confirmation-code")
public class GetCodeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        PrintWriter writer = null;

        String password = (String) req.getSession().getAttribute(PASSWORD);
        String username = (String) req.getSession().getAttribute(USERNAME);
        String email = (String) req.getSession().getAttribute(EMAIL);
        int userId = (int) req.getSession().getAttribute(USER_ID);

        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(username);
        user.setUserId(userId);
        Identification id = new Identification();
        id.setUser(user);

        String token = (String) req.getSession().getAttribute(TOKEN);
        if (token == null) token = "";

        InitUrlConnection<Identification, Report> connection = new InitUrlConnection<>();
        Report report = connection.getReader(id, GENERATE_CODE, token);

        if (report != null) {
            if (report.getStatus() != 200) {
                String jsonStr = gson.toJson(report);
                resp.setStatus(report.getStatus());
                writer = resp.getWriter();
                writer.write(jsonStr);
            } else {
                String jsonStr = gson.toJson(report);
                resp.setStatus(report.getStatus());
                writer = resp.getWriter();
                writer.write(jsonStr);
            }
        }
    }
}
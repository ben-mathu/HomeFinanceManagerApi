package com.benardmathu.hfms.view.registration;

import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.CHANGE_EMAIL;
import static com.benardmathu.hfms.data.utils.URL.REGISTRATION;

/**
 * @author bernard
 */
@WebServlet(CHANGE_EMAIL)
public class ChangeEmailServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String email = req.getParameter(EMAIL);
        String username = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);
        String token = getToken(req);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(user, REGISTRATION, token, "POST");

        String line = "";
        Report report = null;
        while((line = streamReader.readLine()) != null) {
            report = gson.fromJson(line, Report.class);
        }
        
        if (report != null) {
            String response = gson.toJson(report);
            
            resp.setStatus(report.getStatus());
            writer = resp.getWriter();
            writer.write(response);
        }
    }

    private String getToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie :
                cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
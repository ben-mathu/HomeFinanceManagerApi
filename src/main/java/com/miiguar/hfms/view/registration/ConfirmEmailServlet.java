package com.miiguar.hfms.view.registration;

import com.miiguar.hfms.data.models.user.Identification;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;
import com.miiguar.hfms.view.result.ErrorResults;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static com.miiguar.hfms.data.utils.URL.*;
import static com.miiguar.hfms.utils.Constants.*;

/**
 * @author bernard
 */
@WebServlet(EMAIL_CONFIRMATION)
public class ConfirmEmailServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String code = req.getParameter(CODE);
        String username = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);
        String email = req.getParameter(EMAIL);

        int userId = 0;
        if (req.getSession().getAttribute(USER_ID) != null)
            userId = (int) req.getSession().getAttribute(USER_ID);

        final ErrorResults results = new ErrorResults();
        if (code.length() != 6) {
            results.setCodeError("The code you provided is invalid.");
        } else {
            User user = new User();
            user.setUserId(userId);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            Identification id = new Identification();
            id.setCode(code);
            id.setUser(user);

            String token = getTokenFromCookie(req);
            if (token == null) token = "";

            InitUrlConnection<Identification> urlConnection = new InitUrlConnection<>();
            BufferedReader streamReader = urlConnection.getReader(id, CONFIRM, token);

            String line = "";
            Report item = null;
            while((line = streamReader.readLine()) != null) {
                item = gson.fromJson(line, Report.class);
            }

            if (item != null) {
                if (item.getStatus() != 200) {
//                    String response = gson.toJson(item);

                    resp.setStatus(item.getStatus());
                    writer = resp.getWriter();
                    writer.write(item.getMessage());
                } else {
                    writer = resp.getWriter();
                    String redirect = req.getContextPath() + "/dashboard";
                    writer.write(redirect);
                }
            }
            urlConnection.close();
        }
    }
}
package com.benardmathu.hfms.view.registration;

import com.benardmathu.hfms.data.user.Identification;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.GENERATE_CODE;
import static com.benardmathu.hfms.data.utils.URL.GET_CONFIRMATION_CODE;

/**
 * @author bernard
 */
@WebServlet(GET_CONFIRMATION_CODE)
public class GetCodeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        String password = (String) req.getSession().getAttribute(PASSWORD);
        String username = (String) req.getSession().getAttribute(USERNAME);
        String email = (String) req.getSession().getAttribute(EMAIL);
        String userId = getUserIdFromCookie(req);

        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(username);
        user.setUserId(userId);
        Identification id = new Identification();
        id.setUser(user);

        String token = getTokenFromCookie(req);
        if (token == null) token = "";

        InitUrlConnection<Identification> connection = new InitUrlConnection<>();
        BufferedReader streamReader = connection.getReader(id, GENERATE_CODE, token, "POST");

        String line = "";
        Report report = null;
        while((line = streamReader.readLine()) != null) {
            report = gson.fromJson(line, Report.class);
        }

        if (report != null) {
            String jsonStr = gson.toJson(report);
            resp.setStatus(report.getStatus());
            writer = resp.getWriter();
            writer.write(jsonStr);
        }
    }
}
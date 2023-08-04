package com.benardmathu.hfms.view.registration;

import com.benardmathu.hfms.data.user.Identification;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
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
@Controller(URL.GET_CONFIRMATION_CODE)
public class GetCodeController extends BaseController {
    private static final long serialVersionUID = 1L;

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = (String) req.getSession().getAttribute(DbEnvironment.PASSWORD);
        String username = (String) req.getSession().getAttribute(DbEnvironment.USERNAME);
        String email = (String) req.getSession().getAttribute(DbEnvironment.EMAIL);
        String userId = getUserIdFromCookie(req);

        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(username);
        user.setId(Long.parseLong(userId));
        Identification id = new Identification();
        id.setUser(user);

        String token = getTokenFromCookie(req);
        if (token == null) token = "";

        InitUrlConnection<Identification> connection = new InitUrlConnection<>();
        BufferedReader streamReader = connection.getReader(id, URL.GENERATE_CODE, token, "POST");

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
package com.benardmathu.hfms.view.login;

import com.benardmathu.hfms.data.user.UserRequest;
import com.benardmathu.hfms.data.user.UserResponse;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseController;
import com.benardmathu.hfms.view.result.ErrorResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.LOGIN;
import static com.benardmathu.hfms.utils.Constants.TOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author bernard
 */
@Controller(URL.LOGIN)
public class LoginController extends BaseController {

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        loginUser(req, resp);
    }

    public void loginUser(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        boolean isParamsValid = true;
        PrintWriter writer = null;

        final String username = request.getParameter(DbEnvironment.USERNAME).trim();
        final String password = request.getParameter(DbEnvironment.PASSWORD);

        final ErrorResults results = new ErrorResults();

        if (username.isEmpty() || request.getParameter(DbEnvironment.USERNAME) == null) {
            results.setUsernameError("Username is required!");
            isParamsValid = false;
        }

        if (password.isEmpty() || request.getParameter(DbEnvironment.PASSWORD) == null) {
            results.setPasswordError("Password is required!");
            isParamsValid = false;
        }

        if (isParamsValid) {

            String token = getTokenFromCookie(request);

            final User user = new User();
            user.setAdmin(true);
            user.setEmail("");
            user.setUsername(request.getParameter(DbEnvironment.USERNAME));
            user.setPassword(request.getParameter(DbEnvironment.PASSWORD));
            final UserRequest params = new UserRequest();
            params.setUser(user);

            InitUrlConnection<UserRequest> connection = new InitUrlConnection<>();
            BufferedReader streamReader = connection.getReader(params, URL.LOGIN, "POST");

            String line = "";
            UserResponse item = null;
            while((line = streamReader.readLine()) != null) {
                item = gson.fromJson(line, UserResponse.class);
            }

            if (item != null) {
                if (item.getReport().getStatus() != 200) {
                    String jsonStr = gson.toJson(item.getReport());
                    response.setStatus(item.getReport().getStatus());
                    writer = response.getWriter();
                    writer.write(jsonStr);

                } else {

                    request.getSession().setAttribute(DbEnvironment.USERNAME, item.getUser().getUsername());
                    request.getSession().setAttribute(DbEnvironment.USER_ID, item.getUser().getId());
                    request.getSession().setAttribute(DbEnvironment.EMAIL, item.getUser().getEmail());
                    request.getSession().setAttribute(DbEnvironment.USERNAME, item.getUser().getUsername());
                    request.getSession().setAttribute(DbEnvironment.MOB_NUMBER, item.getUser().getMobNum());
                    request.getSession().setAttribute(TOKEN, item.getReport().getToken());
                    request.getSession().setAttribute("isAlreadySent", false);

                    // save the session
                    Cookie cookie = new Cookie(TOKEN, item.getReport().getToken());
                    response.addCookie(cookie);

                    Cookie subjectCookie = new Cookie(SUBJECT, item.getReport().getSubject());
                    response.addCookie(subjectCookie);

                    request.getSession().setAttribute("isAlreadySent", false);

                    String responseStr = gson.toJson(item);

                    response.setStatus(item.getReport().getStatus());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    writer = response.getWriter();
                    writer.write(responseStr);
                }
            }

            connection.close();
        } else {
            final String error = gson.toJson(results);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer = response.getWriter();
            writer.write(error);
        }
    }
}

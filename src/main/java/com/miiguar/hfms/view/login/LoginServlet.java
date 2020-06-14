package com.miiguar.hfms.view.login;

import com.miiguar.hfms.data.models.user.UserRequest;
import com.miiguar.hfms.data.models.user.UserResponse;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.utils.Patterns;
import com.miiguar.hfms.view.base.BaseServlet;
import com.miiguar.hfms.view.result.ErrorResults;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.miiguar.hfms.data.utils.URL.LOGIN;
import static com.miiguar.hfms.data.utils.URL.REGISTRATION;
import static com.miiguar.hfms.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.TOKEN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet(urlPatterns = LOGIN)
public class LoginServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

        loginUser(req, resp);
    }

    public void loginUser(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        boolean isParamsValid = true;
        PrintWriter writer = null;

        final String username = request.getParameter(USERNAME).trim();
        final String password = request.getParameter(PASSWORD);

        final ErrorResults results = new ErrorResults();

        if (username.isEmpty() || request.getParameter(USERNAME) == null) {
            results.setUsernameError("Username is required!");
            isParamsValid = false;
        }

        if (password.isEmpty() || request.getParameter(PASSWORD) == null) {
            results.setPasswordError("Password is required!");
            isParamsValid = false;
        }

        if (isParamsValid) {

            String token = getTokenFromCookie(request);

            final User user = new User();
            user.setAdmin(true);
            user.setEmail("");
            user.setUsername(request.getParameter(USERNAME));
            user.setPassword(request.getParameter(PASSWORD));
            final UserRequest params = new UserRequest(user);

            InitUrlConnection<UserRequest> connection = new InitUrlConnection<>();
            BufferedReader streamReader = connection.getReader(params, LOGIN);

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

                    request.getSession().setAttribute(USERNAME, item.getUser().getUsername());
                    request.getSession().setAttribute(USER_ID, item.getUser().getUserId());
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
                    response.setContentType(APPLICATION_JSON);
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

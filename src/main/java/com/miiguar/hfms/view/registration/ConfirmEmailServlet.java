package com.miiguar.hfms.view.registration;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.user.Identification;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;
import com.miiguar.hfms.view.result.ErrorResults;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
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

        String userId = req.getParameter(USER_ID);

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

            String token = req.getParameter(TOKEN);
            if (token == null) token = "";

            InitUrlConnection<Identification> urlConnection = new InitUrlConnection<>();
            BufferedReader streamReader = urlConnection.getReader(id, CONFIRM, token, "POST");

            String line = "";
            Report item = null;
            while((line = streamReader.readLine()) != null) {
                item = gson.fromJson(line, Report.class);
            }

            if (item != null) {
                if (item.getStatus() != 200) {
                    String response = gson.toJson(item);

                    resp.setStatus(item.getStatus());
                    writer = resp.getWriter();
                    writer.write(response);
                } else {
                    req.getSession().setAttribute(USERNAME, user.getUsername());
                    req.getSession().setAttribute(EMAIL, user.getEmail());
                    req.getSession().setAttribute(USER_ID, user.getUserId());

//                    resp.sendRedirect("Dashboard");
                    writer = resp.getWriter();
                    String redirect = "/dashboard";
                    TokenResponse tokenResponse = new TokenResponse();
                    tokenResponse.setRedirect(redirect);
                    tokenResponse.setToken(token);
                    String jsonStr = gson.toJson(tokenResponse);
                    writer.write(jsonStr);
                }
            }
            urlConnection.close();
        }
    }

    public class TokenResponse {
        @SerializedName("token")
        private String token = "";
        @SerializedName("redirect")
        private String redirect = "";

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRedirect() {
            return redirect;
        }

        public void setRedirect(String redirect) {
            this.redirect = redirect;
        }
    }
}
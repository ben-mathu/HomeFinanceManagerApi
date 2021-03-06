package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.user.model.User;
import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import com.benardmathu.hfms.data.utils.URL;
import static com.benardmathu.hfms.utils.Constants.TOKEN;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseServlet;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@WebServlet(name = "ChangePasswordController", urlPatterns = {"/dashboard/account-controller/*"})
public class ChangeAccountDetailsController extends BaseServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        
        String userId = request.getParameter(USER_ID);
        String token = request.getParameter(TOKEN);
        String newEmail = request.getParameter(EMAIL);
        String newUSername = request.getParameter(USERNAME);
        String newPassword = request.getParameter(PASSWORD);
        
        User user = new User();
        user.setUserId(userId);
        user.setPassword(newPassword);
        user.setEmail(newEmail);
        user.setUsername(newUSername);
        
        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = null;
        if (uri.endsWith("change-password")) {
            streamReader = conn.getReader(user, URL.CHANGE_ACCOUNT_DETAILS, token, "POST");
        } else if (uri.endsWith("change-email")) {
            streamReader = conn.getReader(user, URL.CHANGE_EMAIL_DETAILS, token, "POST");
        } else if (uri.endsWith("change-username")) {
            streamReader = conn.getReader(user, URL.CHANGE_USERNAME_DETAILS, token, "POST");
        } else if (uri.endsWith("change-number")) {
            streamReader = conn.getReader(user, URL.CHANGE_NUMBER_DETAILS, token, "POST");
        }
        
        if (streamReader != null) {
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = streamReader.readLine()) != null) {            
                builder.append(line);
            }

            writer = response.getWriter();
            writer.write(builder.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        String token = req.getParameter(TOKEN);
        
        User user = new User();
        user.setUserId(userId);
        
        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(user, URL.CHANGE_ACCOUNT_DETAILS, token, "PUT");
        
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        
        while ((line = streamReader.readLine()) != null) {            
            stringBuilder.append(line);
        }
        
        Report report = gson.fromJson(stringBuilder.toString(), Report.class);
        resp.setStatus(report.getStatus());
        
        writer = resp.getWriter();
        writer.write(stringBuilder.toString());
    }
}

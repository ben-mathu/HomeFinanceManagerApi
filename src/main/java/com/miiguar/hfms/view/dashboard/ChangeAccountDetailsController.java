/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miiguar.hfms.view.dashboard;

import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.user.model.User;
import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import com.miiguar.hfms.data.utils.URL;
import static com.miiguar.hfms.utils.Constants.TOKEN;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
        String newPassword = request.getParameter(PASSWORD);
        
        User user = new User();
        user.setUserId(userId);
        user.setPassword(newPassword);
        
        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = null;
        if (uri.endsWith("change-password")) {
            streamReader = conn.getReader(user, URL.CHANGE_ACCOUNT_DETAILS, token, userId);
        } else if (uri.endsWith("change-email")) {
            streamReader = conn.getReader(user, URL.CHANGE_EMAIL_DETAILS, token, userId);
        } else if (uri.endsWith("change-username")) {
            streamReader = conn.getReader(user, URL.CHANGE_USERNAME_DETAILS, token, userId);
        } else if (uri.endsWith("change-number")) {
            streamReader = conn.getReader(user, URL.CHANGE_NUMBER_DETAILS, token, userId);
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
}

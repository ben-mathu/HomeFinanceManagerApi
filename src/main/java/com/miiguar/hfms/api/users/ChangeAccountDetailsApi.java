/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miiguar.hfms.api.users;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.user.UserDao;
import com.miiguar.hfms.data.user.model.User;
import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.*;
import com.miiguar.hfms.utils.BufferRequestReader;
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
@WebServlet(name = "ChangeAccountDetailsApi", urlPatterns = {API + CHANGE_ACCOUNT_URL})
public class ChangeAccountDetailsApi extends BaseServlet {

    private UserDao userDao;
    
    public ChangeAccountDetailsApi() {
        userDao = new UserDao();
    }
    
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
        int affected = 0;
        
        if (uri.endsWith("change-password")) {
            affected = changePassword(request);
        } else if (uri.endsWith("change-email")) {
            affected = changeEmail(request);
        } else if (uri.endsWith("change-username")) {
            affected = changeUsername(request);
        } else if (uri.endsWith("change-number")) {
            affected = changeNumber(request);
        }
        
        if (affected > 0) {
            response.setStatus(HttpServletResponse.SC_OK);
            
            Report report = new Report();
            report.setStatus(response.getStatus());
            report.setMessage("Saved Successfully");
            writer = response.getWriter();
            writer.write(gson.toJson(report));
        }
    }

    public int changeNumber(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getUserId());
        
        user.setMobNum(userRequest.getMobNum());
        return userDao.update(user);
    }
    
    public int changeUsername(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getUserId());
        
        user.setUsername(userRequest.getUsername());
        return userDao.update(user);
    }
    
    public int changeEmail(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getUserId());
        
        user.setEmail(userRequest.getEmail());
        return userDao.update(user);
    }
    
    public int changePassword(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getUserId());
        
        user.setPassword(userRequest.getPassword());
        return userDao.update(user);
    }

}

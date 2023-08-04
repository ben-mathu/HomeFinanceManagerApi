package com.benardmathu.hfms.api.users;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.user.UserService;
import com.benardmathu.hfms.data.user.UserRepository;
import com.benardmathu.hfms.data.user.model.User;

import static com.benardmathu.hfms.data.utils.URL.*;
import com.benardmathu.hfms.utils.BufferRequestReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@RestController
@RequestMapping(name = "ChangeAccountDetailsApi", value = CHANGE_ACCOUNT_URL)
public class ChangeAccountDetailsApi extends BaseController {

    @Autowired
    private UserRepository userRepository;

    private UserService userDao;
    
    public ChangeAccountDetailsApi() {
        userDao = new UserService();
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @PostMapping
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        User user = null;
        
        if (uri.endsWith("change-password")) {
            user = changePassword(request);
        } else if (uri.endsWith("change-email")) {
            user = changeEmail(request);
        } else if (uri.endsWith("change-username")) {
            user = changeUsername(request);
        } else if (uri.endsWith("change-number")) {
            user = changeNumber(request);
        }
        
        if (user != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            
            Report report = new Report();
            report.setStatus(response.getStatus());
            report.setMessage("Saved Successfully");
            writer = response.getWriter();
            writer.write(gson.toJson(report));
        }
    }

    public User changeNumber(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getId());
        
        user.setMobNum(userRequest.getMobNum());
        return userDao.update(user);
    }
    
    public User changeUsername(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getId());
        
        user.setUsername(userRequest.getUsername());
        return userDao.update(user);
    }
    
    public User changeEmail(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getId());
        
        user.setEmail(userRequest.getEmail());
        return userDao.update(user);
    }
    
    public User changePassword(HttpServletRequest request) {
        String requestStr = BufferRequestReader.bufferRequest(request);
        User userRequest = gson.fromJson(requestStr, User.class);
        
        User user = userDao.get(userRequest.getId());
        
        user.setPassword(userRequest.getPassword());
        return userDao.update(user);
    }

    @PutMapping
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestStr = BufferRequestReader.bufferRequest(req);
        
        User user = gson.fromJson(requestStr, User.class);
        
        userDao.delete(user);
        Report report = new Report();
        report.setMessage("User already deleted");
        report.setStatus(HttpServletResponse.SC_ACCEPTED);

        writer = resp.getWriter();
        writer.write(gson.toJson(report));
    }
}

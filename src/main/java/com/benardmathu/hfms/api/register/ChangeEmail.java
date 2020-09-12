package com.benardmathu.hfms.api.register;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.data.user.UserDao;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.CHANGE_EMAIL;

/**
 * @author bernard
 */
@WebServlet(API + CHANGE_EMAIL)
public class ChangeEmail extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        User user = gson.fromJson(requestStr, User.class);

        Report report = new Report();

        if (changeEmail(user) > 0) {
            report.setMessage("Success");
            report.setStatus(HttpServletResponse.SC_OK);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        } else {

            report.setMessage("Error changing email");
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
            Log.d(TAG, "Error changing email");
        }
    }

    private int changeEmail(User user) {
        int affectedRows = userDao.updateEmail(user);
        return 0;
    }
}
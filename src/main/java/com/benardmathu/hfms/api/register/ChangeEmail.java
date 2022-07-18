package com.benardmathu.hfms.api.register;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.user.UserService;
import com.benardmathu.hfms.data.user.UserRepository;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.benardmathu.hfms.data.utils.URL.CHANGE_EMAIL;

/**
 * @author bernard
 */
@RestController
@RequestMapping(CHANGE_EMAIL)
public class ChangeEmail extends BaseController {

    @Autowired
    private UserRepository userRepository;

    private UserService userDao = new UserService();

    @PostMapping
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
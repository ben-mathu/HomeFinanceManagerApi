package com.benardmathu.hfms.api.register;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.code.CodeBaseService;
import com.benardmathu.hfms.data.code.model.Code;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.data.user.Identification;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.BufferRequestReader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.CONFIRM;

/**
 * @author bernard
 */
@RestController
@RequestMapping(API + CONFIRM)
public class Confirm extends BaseController {
    public static final String TAG = Confirm.class.getSimpleName();

    private ConfigureDb db;
    private Properties prop;
    private JdbcConnection jdbcConnection;

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        Identification id = gson.fromJson(requestStr, Identification.class);

        this.db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();
        if (isCodeCorrect(id)) {

            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_OK);
            report.setMessage("Success");
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        } else {
            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            report.setMessage("</br>Sorry that code is invalid</br> Please try again or send another");
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        }
    }

    private boolean isCodeCorrect(Identification id) {
        CodeBaseService dao = new CodeBaseService();
        Code item = dao.get(id.getUser().getUserId().toString());

        if (item.getCode().equals(id.getCode())) {
            dao.emailConfirmed(item.getCode());
            return true;
        }
        return false;
    }
}
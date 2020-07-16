package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.code.CodeDao;
import com.miiguar.hfms.data.code.model.Code;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.user.Identification;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.CONFIRM;

/**
 * @author bernard
 */
@WebServlet(API + CONFIRM)
public class Confirm extends BaseServlet {
    private static final long serialVersionUID = 1L;
    public static final String TAG = Confirm.class.getSimpleName();

    private ConfigureDb db;
    private Properties prop;
    private JdbcConnection jdbcConnection;

    @Override
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
        CodeDao dao = new CodeDao();
        Code item = dao.get(id.getUser().getUserId());

        if (item.getCode().equals(id.getCode())) {
            dao.emailConfirmed(item.getCode());
            return true;
        }
        return false;
    }
}
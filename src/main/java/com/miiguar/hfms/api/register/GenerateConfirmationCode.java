package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.code.CodeDao;
import com.miiguar.hfms.data.code.model.Code;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.user.Identification;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.utils.sender.EmailSession;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.GENERATE_CODE;

/**
 * @author bernard
 */
@WebServlet(API + GENERATE_CODE)
public class GenerateConfirmationCode extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private ConfigureDb db;
    private Properties prop;
    private JdbcConnection jdbcConnection;
    private Connection connection;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);
        Identification user = gson.fromJson(requestStr, Identification.class);

        String code = generateCode();

        ConfigureDb db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();

        try {
            connection = jdbcConnection.getConnection(prop.getProperty("db.main_db"));

            saveCode(code, user.getUser(), connection);
            sendCodeToEmail(user.getUser(), code);

            Report report = new Report();
            report.setMessage("Success");
            report.setStatus(HttpServletResponse.SC_OK);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        } catch (SQLException | MessagingException e) {
            Report report = new Report();
            report.setMessage("Error occurred please send the code again.");
            report.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
            Log.e(TAG, "Error storing confirmation code.", e);
        } finally {
            try {
                closeConnection();
            } catch (SQLException throwables) {
                Log.e(TAG, "An error occurred while closing connection", throwables);
            }
        }
    }

    @Override
    public void closeConnection() throws SQLException {
        if (!connection.isClosed())
            connection.close();
        connection = null;
        jdbcConnection.disconnect();
        jdbcConnection = null;
    }

    public String generateCode() {
        GenerateRandomString rand = new GenerateRandomString(6, new Random(), "0123456789");
        return rand.nextString();
    }

    public void sendCodeToEmail(User user, String code) throws MessagingException {
        EmailSession session = new EmailSession();
        session.sendEmail(
                user.getEmail(),
                "Email Confirmation Code",
                "<div style=\"text-align: center;\">" +
                        "<h5>User this code to confirm your email</h5>" +
                        "<h2>" + code + "</h2>" +
                        "</div>"
        );
    }

    public void saveCode(String code, User user, Connection connection) throws SQLException {
        CodeDao dao = new CodeDao();
        Code item = new Code();
        item.setCode(code);
        int affectedRows = dao.saveCode(item, user.getUserId(), connection);

        Log.d(TAG, "Affected Rows:" + affectedRows);
    }
}
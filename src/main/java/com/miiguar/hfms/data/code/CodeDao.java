package com.miiguar.hfms.data.code;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.code.model.Code;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.utils.sender.EmailSession;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class CodeDao implements Dao<Code> {
    public static final String TAG = CodeDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public CodeDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    public int saveCode(Code item, String userId) {
        String query = "INSERT INTO " + CODE_TB_NAME + "(" +
                CODE + "," + USER_ID + ")" +
                "VALUES (?,?)" +
                " ON CONFLICT (" + USER_ID + ")" +
                " DO UPDATE" +
                " SET " + CODE + "=?"+
                " WHERE " + CODE_TB_NAME + "." + USER_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement insertCode = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insertCode = conn.prepareStatement(query);

            insertCode.setString(1, item.getCode());
            insertCode.setString(2, userId);
            insertCode.setString(3, item.getCode());
            insertCode.setString(4, userId);
            affectedRows = insertCode.executeUpdate();

            insertCode.close();
            insertCode = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing code update", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (insertCode != null)
                try {
                    insertCode.close();
                    insertCode = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }

        return affectedRows;
    }
    @Override
    public int save(Code item) {
        return 0;
    }

    @Override
    public int update(Code item) {
        return 0;
    }

    @Override
    public int delete(Code item) {
        return 0;
    }

    @Override
    public Code get(String id) {
        String query = "SELECT * FROM " + CODE_TB_NAME + " WHERE " + USER_ID + "=?";

        Code item = null;
        ResultSet resultSet = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();
            String code = "";
            item = new Code();
            while (resultSet.next()) {
                item.setCode(resultSet.getString(CODE));
                item.setEmailConfirmed(resultSet.getBoolean(EMAIL_CONFIRMED));
                item.setUserId(id);
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing code query", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (resultSet != null)
                try {
                    resultSet.close();
                    resultSet = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }
        return item;
    }

    @Override
    public List<Code> getAll() {
        return null;
    }

    @Override
    public List<Code> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<Code> items) {
        return 0;
    }

    public int emailConfirmed(String code) {
        String query = "UPDATE " + CODE_TB_NAME + " SET " + EMAIL_CONFIRMED + "=? " + "WHERE " + CODE + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement update = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            update = conn.prepareStatement(query);
            update.setBoolean(1, true);
            update.setString(2, code);
            update.closeOnCompletion();
            affectedRows = update.executeUpdate();

            update.close();
            update = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing email confirmation", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (update != null)
                try {
                    update.close();
                    update = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }
        return affectedRows;
    }

    public void sendCodeToEmail(User user, String code) {
        EmailSession session = new EmailSession();
        try {
            session.sendEmail(
                    user.getEmail(),
                    "Email Confirmation Code",
                    "<div style=\"text-align: center;\">" +
                            "<h5>User this code to confirm your email</h5>" +
                            "<h2>" + code + "</h2>" +
                            "</div>"
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

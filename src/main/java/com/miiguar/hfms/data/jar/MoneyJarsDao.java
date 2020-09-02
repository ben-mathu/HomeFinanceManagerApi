package com.miiguar.hfms.data.jar;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.jar.model.MoneyJar;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.utils.Log;

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
public class MoneyJarsDao implements Dao<MoneyJar> {
    public static final String TAG = MoneyJarsDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public MoneyJarsDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }
    @Override
    public int save(MoneyJar item) {
        String query = "INSERT INTO " + MONEY_JAR_TB_NAME + "(" +
                MONEY_JAR_ID + "," + MONEY_EXPENSE_TYPE + "," + CATEGORY + "," +
                TOTAL_AMOUNT + "," + CREATED_AT + "," + SCHEDULED_FOR + "," +
                SCHEDULED_TYPE + "," + HOUSEHOLD_ID + "," + JAR_STATUS + ")" +
                " VALUES (?,?,?,?,?,?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getMoneyJarId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setString(3, item.getCategory());
            preparedStatement.setDouble(4, item.getTotalAmount());
            preparedStatement.setString(5, item.getCreatedAt());
            preparedStatement.setString(6, item.getScheduledFor());
            preparedStatement.setString(7, item.getScheduleType());
            preparedStatement.setString(8, item.getHouseholdId());
            preparedStatement.setBoolean(9, item.isJarStatus());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing envelope update", throwables);
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
        }
        return affectedRows;
    }

    @Override
    public int update(MoneyJar item) {
        String query = "UPDATE " + MONEY_JAR_TB_NAME +
                " SET " + MONEY_EXPENSE_TYPE + "=?," +
                CATEGORY + "=?," +
                TOTAL_AMOUNT + "=?," +
                CREATED_AT + "=?," +
                SCHEDULED_TYPE + "=?," +
                HOUSEHOLD_ID + "=?," +
                JAR_STATUS + "=?," +
                PAYMENT_STATUS + "=?" +
                " WHERE " + MONEY_JAR_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getCategory());
            preparedStatement.setDouble(3, item.getTotalAmount());
            preparedStatement.setString(4, item.getCreatedAt());
            preparedStatement.setString(5, item.getScheduleType());
            preparedStatement.setString(6, item.getHouseholdId());
            preparedStatement.setBoolean(7, item.isJarStatus());
            preparedStatement.setBoolean(8, item.isPaymentStatus());
            preparedStatement.setString(9, item.getMoneyJarId());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing envelope update", throwables);
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
        }
        return affectedRows;
    }

    @Override
    public int delete(MoneyJar item) {
        String query = "DELETE FROM " + MONEY_JAR_TB_NAME +
                " WHERE " + MONEY_JAR_ID + "=?";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int affectedRows = 0;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getMoneyJarId());

            affectedRows = preparedStatement.executeUpdate();
            
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error deleting expense", throwables);
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
        }
        return affectedRows;
    }

    @Override
    public MoneyJar get(String id) {
        String query = "SELECT * FROM " + MONEY_JAR_TB_NAME +
                " WHERE " + MONEY_JAR_ID + "=?";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        MoneyJar envelope = new MoneyJar();
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                envelope.setMoneyJarId(resultSet.getString(MONEY_JAR_ID));
                envelope.setName(resultSet.getString(MONEY_EXPENSE_TYPE));
                envelope.setTotalAmount(resultSet.getDouble(TOTAL_AMOUNT));
                envelope.setCategory(resultSet.getString(CATEGORY));
                envelope.setScheduleType(resultSet.getString(SCHEDULED_TYPE));
                envelope.setHouseholdId(resultSet.getString(HOUSEHOLD_ID));
                envelope.setCreatedAt(resultSet.getString(CREATED_AT));
            }
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing envelope query", throwables);
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
        return envelope;
    }

    @Override
    public List<MoneyJar> getAll() {
        return null;
    }

    @Override
    public List<MoneyJar> getAll(String id) {
        String query = "SELECT * FROM " + MONEY_JAR_TB_NAME +
                " WHERE " + HOUSEHOLD_ID + "=?";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<MoneyJar> envelopes = new ArrayList<>();
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                MoneyJar envelope = new MoneyJar();
                envelope.setMoneyJarId(resultSet.getString(MONEY_JAR_ID));
                envelope.setName(resultSet.getString(MONEY_EXPENSE_TYPE));
                envelope.setTotalAmount(resultSet.getDouble(TOTAL_AMOUNT));
                envelope.setCategory(resultSet.getString(CATEGORY));
                envelope.setJarStatus(resultSet.getBoolean(JAR_STATUS));
                envelope.setScheduleType(resultSet.getString(SCHEDULED_TYPE));
                envelope.setHouseholdId(resultSet.getString(HOUSEHOLD_ID));
                envelope.setCreatedAt(resultSet.getString(CREATED_AT));
                envelopes.add(envelope);
            }
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing envelope query", throwables);
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
        return envelopes;
    }

    @Override
    public int saveAll(ArrayList<MoneyJar> items) {
        return 0;
    }
}

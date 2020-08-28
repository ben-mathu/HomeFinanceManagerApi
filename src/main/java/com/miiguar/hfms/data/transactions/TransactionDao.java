package com.miiguar.hfms.data.transactions;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.transactions.model.Transaction;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import java.sql.ResultSet;

/**
 * @author bernard
 */
public class TransactionDao implements Dao<Transaction> {
    public static final String TAG = TransactionDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public TransactionDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    @Override
    public int save(Transaction item) {
        String query = "INSERT INTO " + TRANSACTION_TB_NAME + "(" +
                TRANSACTION_ID + "," +
                TRANSACTION_DESCRIPTION + "," +
                PAYMENT_DETAILS + "," +
                AMOUNT + "," +
                PAYMENT_STATUS + "," +
                USER_ID + "," +
                PAYMENT_TIMESTAMP + "," +
                CREATED_AT + ")" +
                " VALUES (?,?,?,?,?,?,?,?)" +
                " ON CONFLICT (" + TRANSACTION_ID + ")" +
                " DO UPDATE" +
                " SET " + TRANSACTION_DESCRIPTION + "=?," +
                PAYMENT_DETAILS + "=?," +
                AMOUNT + "=?," +
                PAYMENT_STATUS + "=?," +
                USER_ID + "=?," +
                PAYMENT_TIMESTAMP + "=?," +
                CREATED_AT + "=?" +
                " WHERE " + TRANSACTION_TB_NAME + "." + TRANSACTION_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement insert = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insert = conn.prepareStatement(query);

            insert.setString(1, item.getId());
            insert.setString(2, item.getTransactionDesc());
            insert.setString(3, item.getPaymentDetails());
            insert.setDouble(4, item.getAmount());
            insert.setBoolean(5, item.isPaymentStatus());
            insert.setString(6, item.getUserId());
            insert.setString(7, item.getPaymentTimestamp());
            insert.setString(8, item.getCreatedAt());
            insert.setString(9, item.getTransactionDesc());
            insert.setString(10, item.getPaymentDetails());
            insert.setDouble(11, item.getAmount());
            insert.setBoolean(12, item.isPaymentStatus());
            insert.setString(13, item.getUserId());
            insert.setString(14, item.getPaymentTimestamp());
            insert.setString(15, item.getCreatedAt());
            insert.setString(16, item.getId());
            affectedRows = insert.executeUpdate();

            insert.close();
            insert = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing adding transaction", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (insert != null)
                try {
                    insert.close();
                    insert = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }

        return affectedRows;
    }

    @Override
    public int update(Transaction item) {
        return 0;
    }

    @Override
    public int delete(Transaction item) {
        return 0;
    }

    @Override
    public Transaction get(String id) {
        return null;
    }

    @Override
    public List<Transaction> getAll() {
        return null;
    }

    @Override
    public List<Transaction> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<Transaction> items) {
        return 0;
    }

    public List<Transaction> getAllByUserId(String userId) {
        String query = "SELECT * FROM " + TRANSACTION_TB_NAME +
                " WHERE " + USER_ID + "=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Transaction> transactions = new ArrayList<>();
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, userId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setAmount(resultSet.getDouble(AMOUNT));
                transaction.setCreatedAt(resultSet.getString(CREATED_AT));
                transaction.setId(resultSet.getString(TRANSACTION_ID));
                transaction.setPaymentDetails(resultSet.getString(PAYMENT_DETAILS));
                transaction.setPaymentStatus(resultSet.getBoolean(PAYMENT_STATUS));
                transaction.setPaymentTimestamp(resultSet.getString(PAYMENT_TIMESTAMP));
                transaction.setTransactionDesc(resultSet.getString(TRANSACTION_DESCRIPTION));
                transaction.setUserId(resultSet.getString(USER_ID));
                transactions.add(transaction);
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing users query", throwables);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (resultSet != null) {
                try {
                    resultSet.close();
                    resultSet = null;
                } catch (SQLException throwables) { /* Intentionally blank. */}
            }
        }
        return transactions;
    }
}

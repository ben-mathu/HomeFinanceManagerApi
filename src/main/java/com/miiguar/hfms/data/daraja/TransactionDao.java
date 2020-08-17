package com.miiguar.hfms.data.daraja;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.daraja.models.Transaction;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.init.Table;
import com.miiguar.hfms.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

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
                CHECKOUT_REQ_ID + "," +
                MERCHANT_REQUEST_ID + "," +
                RESULT_CODE + "," +
                RESULT_DESC + "," +
                CALLBACK_METADATA + ")" +
                " VALUES (?,?,?,?,?)" +
                " ON CONFLICT (" + CHECKOUT_REQ_ID + ")" +
                " DO UPDATE" +
                " SET " + MERCHANT_REQUEST_ID + "=?," +
                RESULT_CODE + "=?," +
                RESULT_DESC + "=?," +
                CALLBACK_METADATA + "=?" +
                " WHERE " + TRANSACTION_TB_NAME + "." + CHECKOUT_REQ_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement insert = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insert = conn.prepareStatement(query);

            insert.setString(2, item.getCheckoutReqId());
            insert.setString(1, item.getMerchantReqId());
            insert.setString(3, item.getResultCode());
            insert.setString(4, item.getResultDesc());
            insert.setString(5, item.getCallbackMetadata());
            insert.setString(6, item.getMerchantReqId());
            insert.setString(7, item.getResultCode());
            insert.setString(8, item.getResultDesc());
            insert.setString(9, item.getCallbackMetadata());
            insert.setString(10, item.getCheckoutReqId());
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
}

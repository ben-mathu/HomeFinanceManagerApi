package com.miiguar.hfms.data.assets;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.assets.model.Assets;
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
 * Member methods to create and update the income table
 * @author bernard
 */
public class AssetsDao implements Dao<Assets> {
    public static final String TAG = AssetsDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public AssetsDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }
//    @Override
//    public int createTable(Connection connection) {
//        PreparedStatement create = connection.prepareStatement(
//                "CREATE TABLE " + INCOME_TB_NAME + "(" +
//                        INCOME_ID + " VARCHAR(12)," +
//                        INCOME_DESCRIPTION + " TEXT NOT NULL," +
//                        AMOUNT + " NUMERIC(8,2) NOT NULL," +
//                        USER_ID + " VARCHAR(12) UNIQUE," +
//                        CREATED_AT + " TIMESTAMP NOT NULL," +
//                        " CONSTRAINT " + PRIV_KEY_INCOME + " PRIMARY KEY (" + INCOME_ID + ")"
//        );
//        return 0;
//    }

    @Override
    public int save(Assets item) {
        String query = "INSERT INTO " + ASSET_TB_NAME + "(" +
                ASSET_ID + "," + AMOUNT + "," + ACCOUNT_TYPE + "," +
                USER_ID + "," + CREATED_AT + ")" +
                " VALUES (?,?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getIncomeId());
            preparedStatement.setDouble(2, item.getAmount());
            preparedStatement.setString(3, item.getAccountType());
            preparedStatement.setString(4, item.getUserId());
            preparedStatement.setString(5, item.getCreatedAt());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error adding income: ", throwables);
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
    public int update(Assets item) {
        return 0;
    }

    @Override
    public int delete(Assets item) {
        return 0;
    }

    @Override
    public Assets get(String id) {
        String query = "SELECT * FROM " + ASSET_TB_NAME +
                " WHERE " + USER_ID + "=?";
        Assets income = new Assets();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                income.setAmount(resultSet.getDouble(AMOUNT));
                income.setCreatedAt(resultSet.getString(CREATED_AT));
                income.setAccountType(resultSet.getString(ACCOUNT_TYPE));
                income.setIncomeId(resultSet.getString(ASSET_ID));
                income.setUserId(resultSet.getString(USER_ID));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing income query", throwables);
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
        return income;
    }

    @Override
    public List<Assets> getAll() {
        return null;
    }

    @Override
    public List<Assets> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<Assets> items) {
        return 0;
    }
}

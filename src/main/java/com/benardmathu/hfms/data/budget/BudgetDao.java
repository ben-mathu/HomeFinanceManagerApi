package com.benardmathu.hfms.data.budget;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.Dao;
import com.benardmathu.hfms.data.budget.model.Budget;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class BudgetDao implements Dao<Budget> {
    public static final String TAG = BudgetDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public BudgetDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    public Budget getBudgetByHouseholdId(String id) {
        Budget budget = new Budget();
        String query = "SELECT * FROM " + BUDGET_TB_NAME +
                " WHERE " + HOUSEHOLD_ID + "=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                budget.setBudgetId(resultSet.getString(BUDGET_ID));
                budget.setBudgetAmount(resultSet.getString(BUDGET_AMOUNT));
                budget.setBudgetDesc(resultSet.getString(BUDGET_DESC));
                budget.setHouseholdId(id);
                budget.setCreatedAt(resultSet.getString(CREATED_AT));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user query", throwables);
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

        return budget;
    }

    @Override
    public int save(Budget item) {
        String query = "INSERT INTO " + BUDGET_TB_NAME + "(" +
                BUDGET_ID + "," + BUDGET_AMOUNT + "," + BUDGET_DESC + "," + HOUSEHOLD_ID + "," +
                CREATED_AT + ")" +
                " VALUES (?,?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getBudgetId());
            preparedStatement.setString(2, item.getBudgetAmount());
            preparedStatement.setString(3, item.getBudgetDesc());
            preparedStatement.setString(4, item.getHouseholdId());
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
    public int update(Budget item) {
        return 0;
    }

    @Override
    public int delete(Budget item) {
        return 0;
    }

    @Override
    public Budget get(String id) {
        return null;
    }

    @Override
    public List<Budget> getAll() {
        return null;
    }

    @Override
    public List<Budget> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<Budget> items) {
        return 0;
    }
}

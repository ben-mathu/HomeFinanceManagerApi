package com.miiguar.hfms.data.expense;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.expense.model.Expense;
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
public class ExpenseDao implements Dao<Expense> {
    public static final String TAG = ExpenseDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public ExpenseDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }
    @Override
    public int save(Expense item) {
        String query = "INSERT INTO " + EXPENSES_TB_NAME + "(" +
                EXPENSE_ID + "," + EXPENSE_NAME + "," + EXPENSE_DESCRIPTION + "," +
                AMOUNT + "," + MONEY_JAR_ID + "," +
                PAYEE_NAME + "," + BUSINESS_NUMBER + "," + PHONE_NUMBER + ")" +
                " VALUES (?,?,?,?,?,?,?,?)";

        int affectedRows = 0;
        Connection conn = null;
        PreparedStatement insert = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insert = conn.prepareStatement(query);

            insert.setString(1, item.getExpenseId());
            insert.setString(2, item.getName());
            insert.setString(3, item.getDescription());
            insert.setDouble(4, item.getAmount());
            insert.setString(5, item.getJarId());
            insert.setString(6, item.getPayee());
            insert.setString(7, item.getBusinessNumber());
            insert.setString(8, item.getPhoneNumber());

            affectedRows = insert.executeUpdate();

            insert.close();
            insert = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing adding expense", throwables);
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
    public int update(Expense item) {
        return 0;
    }

    @Override
    public int delete(Expense item) {
        return 0;
    }

    @Override
    public Expense get(String id) {
        String query = "SELECT * FROM " + EXPENSES_TB_NAME +
                " WHERE " + MONEY_JAR_ID + "=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Expense expense = new Expense();
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                expense.setExpenseId(resultSet.getString(EXPENSE_ID));
                expense.setAmount(resultSet.getDouble(AMOUNT));
                expense.setBusinessNumber(resultSet.getString(BUSINESS_NUMBER));
                expense.setDescription(resultSet.getString(EXPENSE_DESCRIPTION));
                expense.setName(resultSet.getString(EXPENSE_NAME));
                expense.setPayee(resultSet.getString(PAYEE_NAME));
                expense.setPhoneNumber(resultSet.getString(PHONE_NUMBER));
                expense.setJarId(resultSet.getString(MONEY_JAR_ID));
            }

            conn.close();
            conn = null;
            preparedStatement.close();
            preparedStatement = null;
            resultSet.close();
            resultSet = null;
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
        return expense;
    }

    @Override
    public List<Expense> getAll() {
        return null;
    }

    @Override
    public List<Expense> getAll(String id) {
        ArrayList<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM " + EXPENSES_TB_NAME +
                " WHERE " + MONEY_JAR_ID + "=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Expense expense = new Expense();
                expense.setExpenseId(resultSet.getString(EXPENSE_ID));
                expense.setAmount(resultSet.getDouble(AMOUNT));
                expense.setBusinessNumber(resultSet.getString(BUSINESS_NUMBER));
                expense.setDescription(resultSet.getString(EXPENSE_DESCRIPTION));
                expense.setName(resultSet.getString(EXPENSE_NAME));
                expense.setPayee(resultSet.getString(PAYEE_NAME));
                expense.setPhoneNumber(resultSet.getString(PHONE_NUMBER));
                expense.setJarId(resultSet.getString(MONEY_JAR_ID));
                expenses.add(expense);
            }

            conn.close();
            conn = null;
            preparedStatement.close();
            preparedStatement = null;
            resultSet.close();
            resultSet = null;
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
        return expenses;
    }

    @Override
    public int saveAll(ArrayList<Expense> items) {
        return 0;
    }
}

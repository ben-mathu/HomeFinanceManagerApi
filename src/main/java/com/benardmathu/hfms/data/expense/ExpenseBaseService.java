package com.benardmathu.hfms.data.expense;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.expense.model.Expense;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
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
public class ExpenseBaseService implements BaseService<Expense> {
    public static final String TAG = ExpenseBaseService.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public ExpenseBaseService() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }
    @Override
    public Expense save(Expense item) {
        String query = "INSERT INTO " + EXPENSES_TB_NAME + "(" +
                EXPENSE_ID + "," + AMOUNT + "," + MONEY_JAR_ID + "," +
                PAYEE_NAME + "," + BUSINESS_NUMBER + "," + ACCOUNT_NUMBER + ")" +
                " VALUES (?,?,?,?,?,?)";

        int affectedRows = 0;
        Connection conn = null;
        PreparedStatement insert = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insert = conn.prepareStatement(query);

            insert.setString(1, item.getExpenseId());
//            insert.setString(2, item.getName());
//            insert.setString(3, item.getDescription());
            insert.setDouble(2, item.getAmount());
            insert.setString(3, item.getJarId());
            insert.setString(4, item.getPayee());
            insert.setString(5, item.getBusinessNumber());
            insert.setString(6, item.getAccountNumber());

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
        String query = "UPDATE " + EXPENSES_TB_NAME +
                " SET " +
                AMOUNT + "=?," +
                MONEY_JAR_ID + "=?," +
                PAYEE_NAME + "=?," +
                BUSINESS_NUMBER + "=?," +
                ACCOUNT_NUMBER + "=?" +
                " WHERE " + EXPENSE_ID + "=?";

        int affectedRows = 0;
        Connection conn = null;
        PreparedStatement insert = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insert = conn.prepareStatement(query);

//            insert.setString(1, item.getName());
//            insert.setString(2, item.getDescription());
            insert.setDouble(1, item.getAmount());
            insert.setString(2, item.getJarId());
            insert.setString(3, item.getPayee());
            insert.setString(4, item.getBusinessNumber());
            insert.setString(5, item.getAccountNumber());
            insert.setString(6, item.getExpenseId());

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
    public int delete(Expense item) {
        return 0;
    }

    @Override
    public Expense get(String id) {
        String query = "SELECT * FROM " + EXPENSES_TB_NAME +
                " WHERE " + EXPENSE_ID + "=?";

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
                expense.setPayee(resultSet.getString(PAYEE_NAME));
                expense.setAccountNumber(resultSet.getString(ACCOUNT_NUMBER));
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
//                expense.setDescription(resultSet.getString(EXPENSE_DESCRIPTION));
//                expense.setName(resultSet.getString(EXPENSE_NAME));
                expense.setPayee(resultSet.getString(PAYEE_NAME));
                expense.setAccountNumber(resultSet.getString(ACCOUNT_NUMBER));
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

package com.benardmathu.hfms.data.income.model;

import com.benardmathu.hfms.data.BaseDao;
import static com.benardmathu.hfms.data.utils.DbEnvironment.AMOUNT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.CREATED_AT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.INCOME_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.ON_CHANGE_INCOME_STATUS;
import static com.benardmathu.hfms.data.utils.DbEnvironment.ON_UPDATE_INCOME;
import com.benardmathu.hfms.utils.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bernard
 */
public class IncomeChangeDao extends BaseDao<OnInComeChange> {

    @Override
    public OnInComeChange get(String id) {
        String query = "SELECT * FROM " + ON_UPDATE_INCOME +
                " WHERE " + INCOME_ID + "=?";
        OnInComeChange income = new OnInComeChange();
        
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
                income.setIncomeId(resultSet.getString(INCOME_ID));
                income.setOnChangeStatus(resultSet.getBoolean(ON_CHANGE_INCOME_STATUS));
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
    
    
    
    public List<OnInComeChange> getAll(String id, String from, String to) {
        String query = "SELECT * FROM " + ON_UPDATE_INCOME +
                " WHERE " + INCOME_ID + "=? AND "
                + CREATED_AT + " BETWEEN SYMMETRIC ? AND ?";
        List<OnInComeChange> list = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, from);
            preparedStatement.setString(3, to);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                OnInComeChange income = new OnInComeChange();
                income.setAmount(resultSet.getDouble(AMOUNT));
                income.setCreatedAt(resultSet.getString(CREATED_AT));
                income.setIncomeId(resultSet.getString(INCOME_ID));
                income.setOnChangeStatus(resultSet.getBoolean(ON_CHANGE_INCOME_STATUS));
                list.add(income);
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
        return list;
    }

    @Override
    public int save(OnInComeChange item) {
        String query = "INSERT INTO " + ON_UPDATE_INCOME + "("
                + AMOUNT + "," + INCOME_ID + "," + CREATED_AT + "," + ON_CHANGE_INCOME_STATUS + ")" +
                " VALUES (?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setDouble(1, item.getAmount());
            preparedStatement.setString(2, item.getIncomeId());
            preparedStatement.setString(3, item.getCreatedAt());
            preparedStatement.setBoolean(4, false);
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
    public int update(OnInComeChange item) {
        String query = "UPDATE " + ON_UPDATE_INCOME
                + " SET " + AMOUNT + "=?,"
                + CREATED_AT + "=?,"
                + ON_CHANGE_INCOME_STATUS + "=?"
                + " WHERE " + INCOME_ID + "=?,";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setDouble(1, item.getAmount());
            preparedStatement.setString(2, item.getCreatedAt());
            preparedStatement.setBoolean(3, false);
            preparedStatement.setString(4, item.getIncomeId());
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
}

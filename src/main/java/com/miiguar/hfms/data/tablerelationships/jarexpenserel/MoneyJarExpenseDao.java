package com.miiguar.hfms.data.tablerelationships.jarexpenserel;

import com.miiguar.hfms.data.BaseDao;
import com.miiguar.hfms.data.utils.DbEnvironment;
import com.miiguar.hfms.utils.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class extends BaseDao
 * @author bernard
 */
public class MoneyJarExpenseDao extends BaseDao<MoneyJarExpenseRel> {

    @Override
    public int save(MoneyJarExpenseRel item) {
        String query = "INSERT INTO " + DbEnvironment.MONEY_JAR_EXPENSE_REL_TB + " (" +
                DbEnvironment.MONEY_JAR_ID + "," + DbEnvironment.EXPENSE_ID + ") " +
                "VALUES (?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setString(1, item.getJarId());
            preparedStatement.setString(2, item.getExpenseId());

            affectedRows = preparedStatement.executeUpdate();

            conn.close();
            conn = null;
            preparedStatement.close();
            preparedStatement = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user update", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }

            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
        }

        return affectedRows;
    }

    public String getIdByJarId(String jarId) {
        String query = "SELECT " + DbEnvironment.EXPENSE_ID + " FROM " + DbEnvironment.MONEY_JAR_EXPENSE_REL_TB +
                " WHERE " + DbEnvironment.MONEY_JAR_ID + "=?";
        
        String id = "";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, jarId);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                id = resultSet.getString(DbEnvironment.EXPENSE_ID);
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
        return id;
    }
}

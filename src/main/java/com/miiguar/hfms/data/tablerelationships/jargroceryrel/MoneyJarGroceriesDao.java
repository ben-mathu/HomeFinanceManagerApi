package com.miiguar.hfms.data.tablerelationships.jargroceryrel;

import com.miiguar.hfms.data.BaseDao;
import com.miiguar.hfms.data.utils.DbEnvironment;
import com.miiguar.hfms.utils.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access objects for MoneyJarGroceriesRel
 * @author bernard
 */
public class MoneyJarGroceriesDao extends BaseDao<MoneyJarGroceriesRel>{
    
    @Override
    public int save(MoneyJarGroceriesRel moneyJarListRel) {
        String query = "INSERT INTO " + DbEnvironment.MONEY_JAR_LIST_REL_TB_NAME + " (" +
                DbEnvironment.MONEY_JAR_ID + "," + DbEnvironment.GROCERY_ID + ") " +
                "VALUES (?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setString(1, moneyJarListRel.getJarId());
            preparedStatement.setString(2, moneyJarListRel.getGroceryId());

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

    public List<String> getIdByJarId(String jarId) {
        String query = "SELECT " + DbEnvironment.GROCERY_ID + " FROM " + DbEnvironment.MONEY_JAR_LIST_REL_TB_NAME +
                " WHERE " + DbEnvironment.MONEY_JAR_ID + "=?";
        
        List<String> ids = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, jarId);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                ids.add(resultSet.getString(DbEnvironment.GROCERY_ID));
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
        return ids;
    }
}

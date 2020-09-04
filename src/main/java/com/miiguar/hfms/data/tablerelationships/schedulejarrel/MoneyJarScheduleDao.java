package com.miiguar.hfms.data.tablerelationships.schedulejarrel;

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
 * 
 * @author bernard
 */
public class MoneyJarScheduleDao extends BaseDao<JarScheduleDateRel> {

    @Override
    public int save(JarScheduleDateRel item) {
        String query = "INSERT INTO " + DbEnvironment.MONEY_JAR_SCHEDULE_REL_TB + " (" +
                DbEnvironment.HOUSEHOLD_ID + "," +
                DbEnvironment.MONEY_JAR_ID + "," +
                DbEnvironment.JAR_SCHEDULE_DATE + ","
                + DbEnvironment.JAR_STATUS + ","
                + DbEnvironment.PAYMENT_STATUS
                + ") " +
                "VALUES (?,?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setString(1, item.getHouseholdId());
            preparedStatement.setString(2, item.getJarId());
            preparedStatement.setString(3, item.getScheduleDate());
            preparedStatement.setBoolean(4, item.isJarStatus());
            preparedStatement.setBoolean(5, item.isPaymentStatus());

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

    @Override
    public int update(JarScheduleDateRel item) {
        String query = "UPDATE " + DbEnvironment.MONEY_JAR_SCHEDULE_REL_TB + " SET " +
                DbEnvironment.HOUSEHOLD_ID + "=?," +
                DbEnvironment.JAR_SCHEDULE_DATE + "=?,"
                + DbEnvironment.JAR_STATUS + "=?,"
                + DbEnvironment.PAYMENT_STATUS + "=?"
                + " WHERE " + DbEnvironment.MONEY_JAR_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setString(1, item.getHouseholdId());
            preparedStatement.setString(2, item.getScheduleDate());
            preparedStatement.setBoolean(3, item.isJarStatus());
            preparedStatement.setBoolean(4, item.isPaymentStatus());
            preparedStatement.setString(5, item.getJarId());

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

    @Override
    public List<JarScheduleDateRel> getAll(String houseId) {
        String query = "SELECT * FROM " + DbEnvironment.MONEY_JAR_SCHEDULE_REL_TB;
        ArrayList<JarScheduleDateRel> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JarScheduleDateRel jarScheduleDateRel = new JarScheduleDateRel();
                jarScheduleDateRel.setHouseholdId(resultSet.getString(DbEnvironment.HOUSEHOLD_ID));
                jarScheduleDateRel.setJarId(resultSet.getString(DbEnvironment.MONEY_JAR_ID));
                jarScheduleDateRel.setScheduleDate(resultSet.getString(DbEnvironment.JAR_SCHEDULE_DATE));
                jarScheduleDateRel.setJarStatus(resultSet.getBoolean(DbEnvironment.JAR_STATUS));
                jarScheduleDateRel.setPaymentStatus(resultSet.getBoolean(DbEnvironment.PAYMENT_STATUS));
                list.add(jarScheduleDateRel);
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
        return list;
    }

    public JarScheduleDateRel getWithStatusFalse(String moneyJarId) {
        String query = "SELECT * FROM " + DbEnvironment.MONEY_JAR_SCHEDULE_REL_TB +
                " WHERE " + DbEnvironment.MONEY_JAR_ID + "=? AND " +
                DbEnvironment.JAR_STATUS + "=?";
        JarScheduleDateRel jarScheduleDateRel = new JarScheduleDateRel();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setString(1, moneyJarId);
            preparedStatement.setBoolean(2, false);
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                jarScheduleDateRel.setHouseholdId(resultSet.getString(DbEnvironment.HOUSEHOLD_ID));
                jarScheduleDateRel.setJarId(resultSet.getString(DbEnvironment.MONEY_JAR_ID));
                jarScheduleDateRel.setScheduleDate(resultSet.getString(DbEnvironment.JAR_SCHEDULE_DATE));
                jarScheduleDateRel.setJarStatus(resultSet.getBoolean(DbEnvironment.JAR_STATUS));
                jarScheduleDateRel.setPaymentStatus(resultSet.getBoolean(DbEnvironment.PAYMENT_STATUS));
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
        return jarScheduleDateRel;
    }
}
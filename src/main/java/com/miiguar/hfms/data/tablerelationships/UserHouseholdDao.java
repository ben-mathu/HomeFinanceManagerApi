package com.miiguar.hfms.data.tablerelationships;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
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
public class UserHouseholdDao implements Dao<UserHouseholdRel> {
    public static final String TAG = UserHouseholdDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public UserHouseholdDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    @Override
    public int save(UserHouseholdRel item) {
        String query = "INSERT INTO " + USER_HOUSEHOLD_TB_NAME + "(" +
                USER_ID + "," + HOUSEHOLD_ID + ")" +
                " VALUES (?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getUserId());
            preparedStatement.setString(2, item.getHouseId());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user household rel update", throwables);
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
    public int update(UserHouseholdRel item) {
        return 0;
    }

    @Override
    public int delete(UserHouseholdRel item) {
        return 0;
    }

    @Override
    public UserHouseholdRel get(String id) {
        String query = "SELECT * FROM " + USER_HOUSEHOLD_TB_NAME +
                " WHERE " + USER_ID + "=?";
        UserHouseholdRel userHouseholdRel = null;

        Connection conn = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userHouseholdRel = new UserHouseholdRel();
                userHouseholdRel.setHouseId(resultSet.getString(HOUSEHOLD_ID));
                userHouseholdRel.setUserId(resultSet.getString(USER_ID));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user household rel query", throwables);
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

        return userHouseholdRel;
    }

    @Override
    public List<UserHouseholdRel> getAll() {
        String query = "SELECT * FROM " + USER_HOUSEHOLD_TB_NAME;
        ArrayList<UserHouseholdRel> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserHouseholdRel userHouseholdRel = new UserHouseholdRel();
                userHouseholdRel.setHouseId(resultSet.getString(HOUSEHOLD_ID));
                userHouseholdRel.setUserId(resultSet.getString(USER_ID));
                list.add(userHouseholdRel);
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user household rel query", throwables);
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
    public List<UserHouseholdRel> getAll(String id) {
        String query = "SELECT * FROM " + USER_HOUSEHOLD_TB_NAME +
                " WHERE " + USER_ID + "=?";
        ArrayList<UserHouseholdRel> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserHouseholdRel userHouseholdRel = new UserHouseholdRel();
                userHouseholdRel.setHouseId(resultSet.getString(HOUSEHOLD_ID));
                userHouseholdRel.setUserId(resultSet.getString(USER_ID));
                list.add(userHouseholdRel);
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user household rel query", throwables);
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
    public int saveAll(ArrayList<UserHouseholdRel> items) {
        return 0;
    }
}

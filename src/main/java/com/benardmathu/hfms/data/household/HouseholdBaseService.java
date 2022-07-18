package com.benardmathu.hfms.data.household;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class HouseholdBaseService implements BaseService<Household> {
    public static final String TAG = HouseholdBaseService.class.getSimpleName();

    @Autowired
    private HouseholdRepository repository;

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public HouseholdBaseService() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    public Household getHousehold(String householdId) {
        Household household = null;
        String query = "SELECT * FROM " + HOUSEHOLD_TB_NAME +
                " WHERE " + HOUSEHOLD_ID + "=?";

        ResultSet resultSet = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, householdId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                household = new Household();
                household.setId(resultSet.getString(HOUSEHOLD_ID));
                household.setName(resultSet.getString(HOUSEHOLD_NAME));
            }

            preparedStatement.close();
            preparedStatement = null;
            resultSet.close();
            resultSet = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing household query", throwables);
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
        return household;
    }

    @Override
    public Household save(Household item) {
        String query = "INSERT INTO " + HOUSEHOLD_TB_NAME + "(" +
                HOUSEHOLD_ID + "," + HOUSEHOLD_NAME + ")" +
                " VALUES (?,?)";

        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getId());
            preparedStatement.setString(2, item.getName());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing income update", throwables);
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
    public int update(Household item) {
        return 0;
    }

    @Override
    public int delete(Household item) {
        repository.delete(item);
        return 1;
    }

    @Override
    public Household get(String id) {
        String query = "SELECT * FROM " + HOUSEHOLD_TB_NAME +
                " WHERE " + HOUSEHOLD_ID + "=?";
        ResultSet resultSet = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        Household household = new Household();
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                household.setId(id);
                household.setName(resultSet.getString(HOUSEHOLD_NAME));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing household query", throwables);
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
        return household;
    }

    @Override
    public List<Household> getAll() {
        return null;
    }

    @Override
    public List<Household> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<Household> items) {
        return 0;
    }

    public String getHouseholdId(String userId) {
        String householdId = "";
        String query = "SELECT " + HOUSEHOLD_ID + " FROM " + USER_HOUSEHOLD_TB_NAME +
                " WHERE " + USER_ID + "=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                householdId = resultSet.getString(HOUSEHOLD_ID);
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        }catch (SQLException e) {
            Log.e(TAG, "Error Processing household id", e);
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
        return householdId;
    }

    public List<String> getUserId(String houseId) {
        List<String> userIdList = new ArrayList<>();
        String query = "SELECT " + USER_ID + " FROM " + USER_HOUSEHOLD_TB_NAME +
                " WHERE " + HOUSEHOLD_ID + "=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, houseId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userIdList.add(resultSet.getString(USER_ID));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        }catch (SQLException e) {
            Log.e(TAG, "Error Processing household id", e);
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
        return userIdList;
    }
}

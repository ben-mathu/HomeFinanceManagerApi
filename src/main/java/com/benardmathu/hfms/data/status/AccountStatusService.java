package com.benardmathu.hfms.data.status;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
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
import java.util.Optional;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Service
public class AccountStatusService implements BaseService<AccountStatus> {
    public static final String TAG = AccountStatusService.class.getSimpleName();

    @Autowired
    private AccountStatusRepository repository;

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;


    public AccountStatusService() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    @Override
    public AccountStatus save(AccountStatus item) {
        String query = "INSERT INTO " + ACCOUNT_STATUS_TB_NAME + "(" +
                USER_ID + ")" +
                " VALUES (?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getUserId());
            affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 1) Log.d(TAG, "Affected Rows: " + affectedRows);

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing account status", throwables);
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
    public int update(AccountStatus item) {
        return 0;
    }

    @Override
    public int delete(AccountStatus item) {
        return 0;
    }

    @Override
    public AccountStatus get(String id) {
        String query = "SELECT * FROM " + ACCOUNT_STATUS_TB_NAME +
                " WHERE " + USER_ID + "=?";
        ResultSet resultSet = null;
        AccountStatus accountStatus = new AccountStatus();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accountStatus.setUserId(id);
                accountStatus.setIncomeStatus(resultSet.getString(INCOME_STATUS));
                accountStatus.setJarStatus(resultSet.getString(JAR_STATUS));
                accountStatus.setAccountStatus(resultSet.getString(ACCOUNT_STATUS));
                accountStatus.setHouseholdStatus(resultSet.getString(HOUSEHOLD_STATUS));
                accountStatus.setReminder(resultSet.getString(COMPLETE_AT));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing account status query", throwables);
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
        return accountStatus;
    }

    @Override
    public List<AccountStatus> getAll() {
        return null;
    }

    @Override
    public List<AccountStatus> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<AccountStatus> items) {
        return 0;
    }

    public boolean updateJarStatus(AccountStatus accountStatus) {
        String query = "UPDATE " + ACCOUNT_STATUS_TB_NAME +
                " SET " + JAR_STATUS + "=?" +
                " WHERE " + USER_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, accountStatus.getJarStatus());
            preparedStatement.setString(2, accountStatus.getUserId());
            affectedRows = preparedStatement.executeUpdate();
            Log.d(TAG, "Affected Rows: " + affectedRows);

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error Processing grocery update", throwables);
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

        return affectedRows == 1;
    }

    public boolean updateIncomeStatus(AccountStatus accountStatus) {
        Optional<AccountStatus> optional = repository.findByUserId(accountStatus.getUserId());
        AccountStatus status = optional.orElse(null);
        status.setIncomeStatus(accountStatus.getIncomeStatus());
        accountStatus = repository.save(accountStatus);
        return false;
    }

    public boolean updateHouseholdStatus(AccountStatus accountStatus) {
        String query = "UPDATE " + ACCOUNT_STATUS_TB_NAME +
                " SET " + HOUSEHOLD_STATUS + "=?" +
                " WHERE " + USER_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, accountStatus.getHouseholdStatus());
            preparedStatement.setString(2, accountStatus.getUserId());
            affectedRows = preparedStatement.executeUpdate();
            Log.d(TAG, "Affected Rows: " + affectedRows);

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
        }
        return affectedRows == 1;
    }
}
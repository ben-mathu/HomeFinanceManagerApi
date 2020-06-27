package com.miiguar.hfms.data.status;

import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class AccountStatusDao implements Dao<AccountStatus> {
    public static final String TAG = AccountStatusDao.class.getSimpleName();
    @Override
    public int save(AccountStatus item, Connection connection) throws SQLException {

        return 0;
    }

    @Override
    public int update(AccountStatus item, Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public int delete(AccountStatus item, Connection connection) {
        return 0;
    }

    @Override
    public AccountStatus get(String id, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<AccountStatus> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<AccountStatus> getAll(String id, Connection connection) throws SQLException {
        return null;
    }

    public boolean updateGroceryStatus(AccountStatus accountStatus, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE " + ACCOUNT_STATUS_TB_NAME +
                        " SET " + GROCERY_STATUS + "=?" +
                        " WHERE " + USER_ID + "=?"
        );
        preparedStatement.setString(1, accountStatus.getGroceryStatus());
        preparedStatement.setString(2, accountStatus.getUserId());
        int affectedRows = preparedStatement.executeUpdate();
        Log.d(TAG, "Affected Rows: " + affectedRows);
        return affectedRows == 1;
    }

    public boolean updateIncomeStatus(AccountStatus accountStatus, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE " + ACCOUNT_STATUS_TB_NAME +
                        " SET " + INCOME_STATUS + "=?" +
                        " WHERE " + USER_ID + "=?"
        );
        preparedStatement.setString(1, accountStatus.getIncomeStatus());
        preparedStatement.setString(2, accountStatus.getUserId());
        int affectedRows = preparedStatement.executeUpdate();
        Log.d(TAG, "Affected Rows: " + affectedRows);
        return affectedRows == 1;
    }

    public boolean updateHouseholdStatus(AccountStatus accountStatus, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE " + ACCOUNT_STATUS_TB_NAME +
                        " SET " + HOUSEHOLD_STATUS + "=?" +
                        " WHERE " + USER_ID + "=?"
        );
        preparedStatement.setString(1, accountStatus.getHouseholdStatus());
        preparedStatement.setString(2, accountStatus.getUserId());
        int affectedRows = preparedStatement.executeUpdate();
        Log.d(TAG, "Affected Rows: " + affectedRows);
        return affectedRows == 1;
    }
}

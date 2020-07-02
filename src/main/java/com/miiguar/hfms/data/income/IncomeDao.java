package com.miiguar.hfms.data.income;

import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.income.model.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * Member methods to create and update the income table
 * @author bernard
 */
public class IncomeDao implements Dao<Income> {
//    @Override
//    public int createTable(Connection connection) {
//        PreparedStatement create = connection.prepareStatement(
//                "CREATE TABLE " + INCOME_TB_NAME + "(" +
//                        INCOME_ID + " VARCHAR(12)," +
//                        INCOME_DESCRIPTION + " TEXT NOT NULL," +
//                        AMOUNT + " NUMERIC(8,2) NOT NULL," +
//                        USER_ID + " VARCHAR(12) UNIQUE," +
//                        CREATED_AT + " TIMESTAMP NOT NULL," +
//                        " CONSTRAINT " + PRIV_KEY_INCOME + " PRIMARY KEY (" + INCOME_ID + ")"
//        );
//        return 0;
//    }

    @Override
    public int save(Income item, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + INCOME_TB_NAME + "(" +
                        INCOME_ID + "," + AMOUNT + "," + ACCOUNT_TYPE + "," +
                        USER_ID + "," + CREATED_AT + ")" +
                        " VALUES (?,?,?,?,?)"
        );

        preparedStatement.setString(1, item.getIncomeId());
        preparedStatement.setDouble(2, item.getAmount());
        preparedStatement.setString(3, item.getAccountType());
        preparedStatement.setString(4, item.getUserId());
        preparedStatement.setString(5, item.getCreatedAt());

        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(Income item, Connection connection) {
        return 0;
    }

    @Override
    public int delete(Income item, Connection connection) {
        return 0;
    }

    @Override
    public Income get(String id, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM " + INCOME_TB_NAME +
                        " WHERE " + USER_ID + "=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Income income = new Income();
        while(resultSet.next()) {
            income.setAmount(resultSet.getDouble(AMOUNT));
            income.setCreatedAt(resultSet.getString(CREATED_AT));
            income.setAccountType(resultSet.getString(ACCOUNT_TYPE));
            income.setIncomeId(resultSet.getString(INCOME_ID));
            income.setUserId(resultSet.getString(USER_ID));
        }
        return income;
    }

    @Override
    public List<Income> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Income> getAll(String id, Connection connection) throws SQLException {
        return null;
    }
}

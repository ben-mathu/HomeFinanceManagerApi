package com.miiguar.hfms.data.tablerelationships;

import com.miiguar.hfms.data.Dao;

import javax.naming.PartialResultException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class UserHouseholdDao implements Dao<UserHouseholdRel> {

    @Override
    public int save(UserHouseholdRel item, Connection connection) throws SQLException {
        PreparedStatement save = connection.prepareStatement(
                "INSERT INTO " + USER_HOUSEHOLD_TB_NAME + "(" +
                        USER_ID + "," + HOUSEHOLD_ID + ")" +
                        " VALUES (?,?)"
        );
        save.setString(1, item.getUserId());
        save.setString(2, item.getHouseId());
        return save.executeUpdate();
    }

    @Override
    public int update(UserHouseholdRel item, Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public int delete(UserHouseholdRel item, Connection connection) {
        return 0;
    }

    @Override
    public UserHouseholdRel get(String id, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM " + USER_HOUSEHOLD_TB_NAME +
                            " WHERE " + USER_ID + "=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        UserHouseholdRel userHouseholdRel = null;
        while (resultSet.next()) {
            userHouseholdRel = new UserHouseholdRel();
            userHouseholdRel.setHouseId(resultSet.getString(HOUSEHOLD_ID));
            userHouseholdRel.setUserId(resultSet.getString(USER_ID));
        }
        return userHouseholdRel;
    }

    @Override
    public List<UserHouseholdRel> getAll(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM " + USER_HOUSEHOLD_TB_NAME
        );
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<UserHouseholdRel> list = new ArrayList<>();
        while (resultSet.next()) {
            UserHouseholdRel userHouseholdRel = new UserHouseholdRel();
            userHouseholdRel.setHouseId(resultSet.getString(HOUSEHOLD_ID));
            userHouseholdRel.setUserId(resultSet.getString(USER_ID));
            list.add(userHouseholdRel);
        }
        return list;
    }

    @Override
    public List<UserHouseholdRel> getAll(String id, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM " + USER_HOUSEHOLD_TB_NAME +
                        " WHERE " + USER_ID + "=?"
        );
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<UserHouseholdRel> list = new ArrayList<>();
        while (resultSet.next()) {
            UserHouseholdRel userHouseholdRel = new UserHouseholdRel();
            userHouseholdRel.setHouseId(resultSet.getString(HOUSEHOLD_ID));
            userHouseholdRel.setUserId(resultSet.getString(USER_ID));
            list.add(userHouseholdRel);
        }
        return list;
    }
}

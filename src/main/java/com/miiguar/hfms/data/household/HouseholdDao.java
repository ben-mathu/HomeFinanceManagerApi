package com.miiguar.hfms.data.household;

import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.household.model.Household;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class HouseholdDao implements Dao<Household> {
    public static Household getHousehold(String householdId, Connection connection) throws SQLException {
        Household household = null;

        PreparedStatement householdStm = connection.prepareStatement(
                "SELECT * FROM " + HOUSEHOLD_TB_NAME +
                        " WHERE " + HOUSEHOLD_ID + "=?"
        );
        householdStm.setString(1, householdId);
        ResultSet resultSet = householdStm.executeQuery();

        while (resultSet.next()) {
            household = new Household();
            household.setId(resultSet.getString(HOUSEHOLD_ID));
            household.setName(resultSet.getString(HOUSEHOLD_NAME));
            household.setDescription(resultSet.getString(HOUSEHOLD_DESCRIPTION));
        }
        return household;
    }

    @Override
    public int save(Household item, Connection connection) throws SQLException {
        PreparedStatement save = connection.prepareStatement(
                "INSERT INTO " + HOUSEHOLD_TB_NAME + "(" +
                        HOUSEHOLD_ID + "," + HOUSEHOLD_NAME + "," + HOUSEHOLD_DESCRIPTION + ")" +
                        " VALUES (?,?,?)"
        );
        save.setString(1, item.getId());
        save.setString(2, item.getName());
        save.setString(3, item.getDescription());
        return save.executeUpdate();
    }

    @Override
    public int update(Household item, Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Household item, Connection connection) {
        return 0;
    }

    @Override
    public Household get(String id, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Household> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Household> getAll(String id, Connection connection) throws SQLException {
        return null;
    }
}

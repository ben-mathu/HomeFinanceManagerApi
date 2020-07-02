package com.miiguar.hfms.data.grocery;

import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class GroceryDao implements Dao<Grocery> {
    public static final String TAG = GroceryDao.class.getSimpleName();

    public static boolean save(Grocery grocery, String groceryId, Connection connection) throws SQLException {
        PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO " + GROCERIES_TB_NAME + "(" +
                        GROCERY_ID + "," +
                        GROCERY_NAME + "," +
                        GROCERY_DESCRIPTION + "," +
                        GROCERY_PRICE + "," +
                        REQUIRED_QUANTITY + "," +
                        REMAINING_QUANTITY + "," +
                        HOUSEHOLD_ID + ")" +
                        "VALUES (?,?,?,?,?,?,?)" +
                        " ON CONFLICT (" + GROCERY_ID + ")" +
                        " DO UPDATE" +
                        " SET " + GROCERY_NAME + "=?," +
                            GROCERY_DESCRIPTION + "=?," +
                            GROCERY_PRICE + "=?," +
                            REQUIRED_QUANTITY + "=?," +
                            REMAINING_QUANTITY + "=?," +
                            HOUSEHOLD_ID + "=?" +
                        " WHERE " + GROCERIES_TB_NAME + "." + GROCERY_ID + "=?"
        );
        insert.setString(1, groceryId);
        insert.setString(2, grocery.getName());
        insert.setString(3, grocery.getDescription());
        insert.setDouble(4, grocery.getPrice());
        insert.setInt(5, grocery.getRequired());
        insert.setInt(6, grocery.getRemaining());
        insert.setString(7, grocery.getHouseholdId());
        insert.setString(8, grocery.getName());
        insert.setString(9, grocery.getDescription());
        insert.setDouble(10, grocery.getPrice());
        insert.setInt(11, grocery.getRequired());
        insert.setInt(12, grocery.getRemaining());
        insert.setString(14, grocery.getHouseholdId());
        insert.setString(13, groceryId);
        int affectedRows = insert.executeUpdate();

        if (affectedRows == 1) {
            Log.d(TAG, "Affected rows: " + affectedRows);
            return true;
        } else {
            return false;
        }
    }

    public GroceriesDto getGroceries() {
        return null;
    }

    public static void createTable(Connection connection) throws SQLException {
        PreparedStatement grocery = connection.prepareStatement(
                "CREATE TABLE " + GROCERIES_TB_NAME + " (" +
                        GROCERY_ID + " varchar(12) NOT NULL," +
                        GROCERY_NAME + " varchar(255) NOT NULL UNIQUE," +
                        GROCERY_DESCRIPTION + " text NOT NULL," +
                        GROCERY_PRICE + " numeric(8,2) NOT NULL," +
                        REQUIRED_QUANTITY + " integer NOT NULL," +
                        REMAINING_QUANTITY + " integer NOT NULL," +
                        "CONSTRAINT " + PRIV_KEY_GROCERIES + " PRIMARY KEY (" + GROCERY_ID + "))"
        );
        grocery.execute();
    }

    @Override
    public int save(Grocery item, Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public int update(Grocery item, Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Grocery item, Connection connection) {
        return 0;
    }

    @Override
    public Grocery get(String id, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Grocery> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Grocery> getAll(String id, Connection connection) throws SQLException {
        return null;
    }
}

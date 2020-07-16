package com.miiguar.hfms.data.grocery;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.grocery.model.Grocery;
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
public class GroceryDao implements Dao<Grocery> {
    public static final String TAG = GroceryDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public GroceryDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    public int save(Grocery grocery, String groceryId) {
        String query = "INSERT INTO " + GROCERIES_TB_NAME + "(" +
                GROCERY_ID + "," +
                GROCERY_NAME + "," +
                GROCERY_DESCRIPTION + "," +
                GROCERY_PRICE + "," +
                REQUIRED_QUANTITY + "," +
                REMAINING_QUANTITY + "," +
                ENVELOPE_ID + ")" +
                "VALUES (?,?,?,?,?,?,?)" +
                " ON CONFLICT (" + GROCERY_ID + ")" +
                " DO UPDATE" +
                " SET " + GROCERY_NAME + "=?," +
                GROCERY_DESCRIPTION + "=?," +
                GROCERY_PRICE + "=?," +
                REQUIRED_QUANTITY + "=?," +
                REMAINING_QUANTITY + "=?," +
                ENVELOPE_ID + "=?" +
                " WHERE " + GROCERIES_TB_NAME + "." + GROCERY_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement insert = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insert = conn.prepareStatement(query);

            insert.setString(1, groceryId);
            insert.setString(2, grocery.getName());
            insert.setString(3, grocery.getDescription());
            insert.setDouble(4, grocery.getPrice());
            insert.setInt(5, grocery.getRequired());
            insert.setInt(6, grocery.getRemaining());
            insert.setString(7, grocery.getEnvelopeId());
            insert.setString(8, grocery.getName());
            insert.setString(9, grocery.getDescription());
            insert.setDouble(10, grocery.getPrice());
            insert.setInt(11, grocery.getRequired());
            insert.setInt(12, grocery.getRemaining());
            insert.setString(14, grocery.getEnvelopeId());
            insert.setString(13, groceryId);
            affectedRows = insert.executeUpdate();

            insert.close();
            insert = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing adding grocery", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (insert != null)
                try {
                    insert.close();
                    insert = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }

        return affectedRows;
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
        grocery.closeOnCompletion();
    }

    @Override
    public int save(Grocery item) {
        String query = "INSERT INTO " + GROCERIES_TB_NAME + "(" +
                GROCERY_ID + "," +
                GROCERY_NAME + "," +
                GROCERY_DESCRIPTION + "," +
                GROCERY_PRICE + "," +
                REQUIRED_QUANTITY + "," +
                REMAINING_QUANTITY + "," +
                HOUSEHOLD_ID + "," +
                ENVELOPE_ID + ")" +
                "VALUES (?,?,?,?,?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement insert = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            insert = conn.prepareStatement(query);

            insert.setString(1, item.getGroceryId());
            insert.setString(2, item.getName());
            insert.setString(3, item.getDescription());
            insert.setDouble(4, item.getPrice());
            insert.setInt(5, item.getRequired());
            insert.setInt(6, item.getRemaining());
            insert.setString(8, item.getEnvelopeId());
            affectedRows = insert.executeUpdate();

            insert.close();
            insert = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing adding grocery", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (insert != null)
                try {
                    insert.close();
                    insert = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }

        return affectedRows;
    }

    @Override
    public int update(Grocery item) {
        return 0;
    }

    @Override
    public int delete(Grocery item) {
        return 0;
    }

    @Override
    public Grocery get(String id) {
        return null;
    }

    @Override
    public List<Grocery> getAll() {
        String query = "SELECT * FROM " + GROCERIES_TB_NAME;
        ArrayList<Grocery> groceries = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                Grocery grocery = new Grocery();
                grocery.setGroceryId(result.getString(GROCERY_ID));
                grocery.setName(result.getString(GROCERY_NAME));
                grocery.setDescription(result.getString(GROCERY_DESCRIPTION));
                grocery.setPrice(result.getDouble(GROCERY_PRICE));
                grocery.setRequired(result.getInt(REQUIRED_QUANTITY));
                grocery.setRemaining(result.getInt(REMAINING_QUANTITY));
                groceries.add(grocery);
            }

            result.close();
            result = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error whilst getting groceries.", throwables);
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

            if (result != null)
                try {
                    result.close();
                    result = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }
        return groceries;
    }

    @Override
    public List<Grocery> getAll(String id) {
        String query = "SELECT * FROM " + GROCERIES_TB_NAME +
                " WHERE " + ENVELOPE_ID + "=?";
        ArrayList<Grocery> groceries = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                Grocery grocery = new Grocery();
                grocery.setGroceryId(result.getString(GROCERY_ID));
                grocery.setName(result.getString(GROCERY_NAME));
                grocery.setDescription(result.getString(GROCERY_DESCRIPTION));
                grocery.setPrice(result.getDouble(GROCERY_PRICE));
                grocery.setRequired(result.getInt(REQUIRED_QUANTITY));
                grocery.setRemaining(result.getInt(REMAINING_QUANTITY));
                grocery.setEnvelopeId(id);
                groceries.add(grocery);
            }

            result.close();
            result = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error whilst getting groceries.", throwables);
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

            if (result != null)
                try {
                    result.close();
                    result = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }
        return groceries;
    }

    @Override
    public int saveAll(ArrayList<Grocery> items) {
        return 0;
    }
}

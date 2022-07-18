package com.benardmathu.hfms.data.grocery;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.grocery.model.Grocery;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;

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
public class GroceryBaseService implements BaseService<Grocery> {
    public static final String TAG = GroceryBaseService.class.getSimpleName();

    @Autowired
    private GroceryRepository repo;

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
    public Grocery save(Grocery item) {
        return repo.save(item);
    }

    @Override
    public Grocery update(Grocery item) {
        return repo.save(item);
    }

    @Override
    public void delete(Grocery item) {
        repo.delete(item);
    }

    @Override
    public Grocery get(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Grocery> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Grocery> saveAll(ArrayList<Grocery> items) {
        return repo.saveAll(items);
    }
}

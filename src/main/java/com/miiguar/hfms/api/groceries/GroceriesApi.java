package com.miiguar.hfms.api.groceries;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.grocery.GroceriesDto;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.data.grocery.GroceryDao;
import com.miiguar.hfms.data.grocery.GroceryDto;
import com.miiguar.hfms.data.household.HouseholdDao;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.status.Status;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.*;
import static com.miiguar.hfms.utils.Constants.COMPLETE;
import static com.miiguar.hfms.utils.Constants.DATE_FORMAT;

/**
 * @author bernard
 */
@WebServlet(API + GROCERIES)
public class GroceriesApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    AccountStatusDao accountStatusDao = new AccountStatusDao();
    HouseholdDao householdDao = new HouseholdDao();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;
    private Connection connection;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        String groceryId = "";
        GroceryDto dto = gson.fromJson(requestStr, GroceryDto.class);
        Grocery grocery = dto.getGrocery();
        User user = dto.getUser();

        GenerateRandomString randomString = new GenerateRandomString(
                12,
                new SecureRandom(),
                GenerateRandomString.getAlphaNumeric()
        );
        groceryId = grocery.getGroceryId().isEmpty() ? randomString.nextString() : grocery.getGroceryId();

        // Initialize db connection properties
        this.db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();

        String uri = req.getRequestURI();
        if (uri.endsWith(ADD_GROCERY)) {
            try {
                connection = jdbcConnection.getConnection(prop.getProperty("db.main_db"));
                // get house id using user id
                String houseId = getHouseId(user.getUserId(), connection);
                grocery.setHouseholdId(houseId);
                if (GroceryDao.save(grocery, groceryId, connection)) {
                    grocery.setGroceryId(groceryId);
                    dto.setGrocery(grocery);
                    String responseStr = gson.toJson(dto);

                    writer = resp.getWriter();
                    writer.write(responseStr);

                    updateStatus(user.getUserId());
                } else {
                    Report report = new Report();
                    report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    report.setMessage("The item was not added.");

                    String responseStr = gson.toJson(report);
                    writer = resp.getWriter();
                    writer.write(responseStr);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    closeConnection();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    private String getHouseId(String userId, Connection connection) throws SQLException {
        return householdDao.getHouseholdId(userId, connection);
    }

    private void updateStatus(String userId) throws SQLException {
        AccountStatus accountStatus = new AccountStatus();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        Status status = new Status();
        status.status = COMPLETE;
        status.date = today;

        String statusStr = gson.toJson(status);
        accountStatus.setGroceryStatus(statusStr);
        accountStatus.setUserId(userId);

        if (accountStatusDao.updateGroceryStatus(accountStatus, connection))
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        this.db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();
        try {
            connection = jdbcConnection.getConnection(prop.getProperty("db.main_db"));

            ArrayList<Grocery> groceries = new ArrayList<>();
            groceries = getAllItems(connection);

            GroceriesDto dto = new GroceriesDto();
            dto.setGroceries(groceries);
            String response = gson.toJson(dto);

            writer = resp.getWriter();
            writer.write(response);
        } catch (SQLException throwables) {
            Log.e(TAG, "Error whilst getting groceries.", throwables);
        } finally {
            try {
                closeConnection();
            } catch (SQLException throwables) {
                Log.e(TAG, "An error occurred while closing connection", throwables);
            }
        }
    }

    private ArrayList<Grocery> getAllItems(Connection connection) throws SQLException {
        PreparedStatement getAll = connection.prepareStatement(
                "SELECT * FROM " + GROCERIES_TB_NAME
        );

        ArrayList<Grocery> groceries = new ArrayList<>();
        ResultSet result = getAll.executeQuery();
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
        return groceries;
    }

    @Override
    public void closeConnection() throws SQLException {
        if (!connection.isClosed())
            connection.close();
        connection = null;
        jdbcConnection.disconnect();
        jdbcConnection = null;
    }
}
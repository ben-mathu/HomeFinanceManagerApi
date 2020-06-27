package com.miiguar.hfms.api.users;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.household.HouseholdDao;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.income.IncomeDao;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.user.UserDao;
import com.miiguar.hfms.data.user.UserDto;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.*;

/**
 * @author bernard
 */
@WebServlet(API + USER_DETAILS)
public class UserApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private IncomeDao incomeDao = new IncomeDao();
    private HouseholdDao householdDao = new HouseholdDao();
    private UserHouseholdDao userHouseholdDao = new UserHouseholdDao();
    private ConfigureDb db;
    private Properties prop;
    private JdbcConnection jdbcConnection;
    private Connection connection;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ConfigureDb db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();

        //get user details
        // get income
        // get household

        String username = req.getParameter(USERNAME);
        try {
            connection = jdbcConnection.getConnection(prop.getProperty("db.main_db"));
            User user = UserDao.getUserDetails(connection, username);
            Income income = incomeDao.get(user.getUserId(), connection);
            List<UserHouseholdRel> list = userHouseholdDao.getAll(user.getUserId(), connection);

            ArrayList<Household> households = new ArrayList<>();
            for (UserHouseholdRel userHouseholdRel: list) {
                Household household = getHousehold(userHouseholdRel, connection);
                households.add(household);
            }

            UserDto dto = new UserDto();
            dto.setUser(user);
            dto.setIncome(income);
            dto.setHouseholds(households);

            String response = gson.toJson(dto);

            writer = resp.getWriter();
            writer.write(response);

            closeConnection();
        } catch (SQLException throwables) {
            Log.e(TAG, "Error retrieving user details", throwables);
        }

    }

    private Household getHousehold(UserHouseholdRel item, Connection connection) {
        Household household = new Household();
        try {
            household = householdDao.get(item.getHouseId(), connection);
        } catch (SQLException throwables) {
            Log.e(TAG + "/ getHousehold ", " Error whilst getting household.", throwables);
        }
        return household;
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
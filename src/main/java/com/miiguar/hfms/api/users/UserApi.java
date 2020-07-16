package com.miiguar.hfms.api.users;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.household.HouseholdDao;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.income.IncomeDao;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.user.UserDao;
import com.miiguar.hfms.data.user.UserDto;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private AccountStatusDao accountStatusDao = new AccountStatusDao();
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get user details
        // get income
        // get household

        String username = req.getParameter(USERNAME);
        User user = userDao.getUserDetails(username);
        Income income = incomeDao.get(user.getUserId());
        List<UserHouseholdRel> list = userHouseholdDao.getAll(user.getUserId());

        ArrayList<Household> households = new ArrayList<>();
        for (UserHouseholdRel userHouseholdRel: list) {
            Household household = getHousehold(userHouseholdRel);
            households.add(household);
        }

        // get account status
        AccountStatus accountStatus = accountStatusDao.get(user.getUserId());

        UserDto dto = new UserDto();
        dto.setUser(user);
        dto.setIncome(income);
        dto.setHouseholds(households);
        dto.setAccountStatus(accountStatus);

        String response = gson.toJson(dto);

        writer = resp.getWriter();
        writer.write(response);
    }

    private Household getHousehold(UserHouseholdRel item) {
        return householdDao.get(item.getHouseId());
    }
}
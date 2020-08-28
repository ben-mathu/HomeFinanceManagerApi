package com.miiguar.hfms.api.users;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.budget.BudgetDao;
import com.miiguar.hfms.data.budget.model.Budget;
import com.miiguar.hfms.data.household.HouseholdDao;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.income.IncomeDao;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.transactions.TransactionDao;
import com.miiguar.hfms.data.transactions.model.Transaction;
import com.miiguar.hfms.data.user.UserDao;
import com.miiguar.hfms.data.user.UserDto;
import com.miiguar.hfms.data.user.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private TransactionDao transactionDao = new TransactionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get user details
        // get income
        // get household

        String userId = req.getParameter(USER_ID);
        User user = userDao.get(userId);
        Income income = incomeDao.get(user.getUserId());
        List<UserHouseholdRel> list = userHouseholdDao.getAll(user.getUserId());

        ArrayList<Household> households = new ArrayList<>();
        ArrayList<User> members = new ArrayList<>();
        for (UserHouseholdRel userHouseholdRel: list) {
            Household household = getHousehold(userHouseholdRel);
            households.add(household);
        }
        
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) getAllTransactions(userId);
        
        for (Household household : households) {
            members.addAll(getUser(household.getId()));
        }

        // get account status
        AccountStatus accountStatus = accountStatusDao.get(user.getUserId());

        UserDto dto = new UserDto();
        dto.setUser(user);
        dto.setIncome(income);
        dto.setHouseholds(households);
        dto.setAccountStatus(accountStatus);
        dto.setUserHouseholdRels((ArrayList<UserHouseholdRel>) list);
        dto.setMembers(members);
        dto.setTransactions(transactions);

        String response = gson.toJson(dto);

        writer = resp.getWriter();
        writer.write(response);
    }

    private List<User> getUser(String houseId) {
        List<String> userIdList = householdDao.getUserId(houseId);
        ArrayList<User> users = new ArrayList<>();
        for (String userId : userIdList) {
            users.add(userDao.get(userId));
        }
        return users;
    }

    private Household getHousehold(UserHouseholdRel item) {
        return householdDao.get(item.getHouseId());
    }
    
    private List<Transaction> getAllTransactions(String userId) {
        return transactionDao.getAllByUserId(userId);
    }
}
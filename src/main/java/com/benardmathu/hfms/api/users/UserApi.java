package com.benardmathu.hfms.api.users;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.data.budget.BudgetDao;
import com.benardmathu.hfms.data.budget.model.Budget;
import com.benardmathu.hfms.data.household.HouseholdDao;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.income.IncomeDao;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.IncomeChangeDao;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.status.AccountStatus;
import com.benardmathu.hfms.data.status.AccountStatusDao;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdDao;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.transactions.TransactionDao;
import com.benardmathu.hfms.data.transactions.model.Transaction;
import com.benardmathu.hfms.data.user.UserDao;
import com.benardmathu.hfms.data.user.UserDto;
import com.benardmathu.hfms.data.user.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.*;

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
    private IncomeChangeDao incomeChangeDao = new IncomeChangeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get user details
        // get income
        // get household

        String userId = req.getParameter(USER_ID);
        User user = userDao.get(userId);
        Income income = incomeDao.get(user.getUserId());
        List<UserHouseholdRel> list = userHouseholdDao.getAllByUserId(user.getUserId());

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
        
        OnInComeChange onInComeChange = incomeChangeDao.get(income.getIncomeId());

        UserDto dto = new UserDto();
        dto.setUser(user);
        dto.setIncome(income);
        dto.setHouseholds(households);
        dto.setAccountStatus(accountStatus);
        dto.setUserHouseholdRels((ArrayList<UserHouseholdRel>) list);
        dto.setMembers(members);
        dto.setTransactions(transactions);
        dto.setIncomeChange(onInComeChange);

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
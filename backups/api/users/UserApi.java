package com.benardmathu.hfms.api.users;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.household.HouseholdService;
import com.benardmathu.hfms.data.household.HouseholdRepository;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.income.IncomeChangeRepository;
import com.benardmathu.hfms.data.income.IncomeService;
import com.benardmathu.hfms.data.income.IncomeRepository;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.IncomeChangeService;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.status.AccountStatus;
import com.benardmathu.hfms.data.status.AccountStatusService;
import com.benardmathu.hfms.data.status.AccountStatusRepository;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdService;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRepository;
import com.benardmathu.hfms.data.transactions.TransactionService;
import com.benardmathu.hfms.data.transactions.TransactionRepository;
import com.benardmathu.hfms.data.transactions.model.Transaction;
import com.benardmathu.hfms.data.user.UserService;
import com.benardmathu.hfms.data.user.UserDto;
import com.benardmathu.hfms.data.user.UserRepository;
import com.benardmathu.hfms.data.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
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
@RestController
@RequestMapping(USER_DETAILS)
public class UserApi extends BaseController {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private UserHouseholdRepository userHouseholdRepository;

    @Autowired
    private AccountStatusRepository accountStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IncomeChangeRepository incomeChangeRepository;

    private IncomeService incomeDao = new IncomeService();
    private HouseholdService householdDao = new HouseholdService();
    private UserHouseholdService userHouseholdDao = new UserHouseholdService();
    private AccountStatusService accountStatusDao = new AccountStatusService();
    private UserService userDao = new UserService();
    private TransactionService transactionDao = new TransactionService();
    private IncomeChangeService incomeChangeService = new IncomeChangeService();

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get user details
        // get income
        // get household

        Long userId = Long.parseLong(req.getParameter(USER_ID));
        User user = userDao.get(userId);
        Income income = incomeDao.get(user.getId());
        List<UserHouseholdRel> list = userHouseholdDao.getAllByUserId(user.getId());

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
        AccountStatus accountStatus = accountStatusDao.get(user.getId());
        
        OnInComeChange onInComeChange = incomeChangeService.get(income.getIncomeId());

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

    private List<User> getUser(Long houseId) {
        List<User> userIdList = householdDao.getUserId(houseId);
        ArrayList<User> users = new ArrayList<>();
        for (User user : userIdList) {
            users.add(userDao.get(user.getId()));
        }
        return users;
    }

    private Household getHousehold(UserHouseholdRel item) {
        return householdDao.get(item.getHouseId());
    }
    
    private List<Transaction> getAllTransactions(Long userId) {
        return transactionDao.getAllByUserId(userId);
    }
}
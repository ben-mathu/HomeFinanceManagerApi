package com.benardmathu.hfms.data.user;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.budget.model.Budget;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.status.AccountStatus;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.transactions.model.Transaction;
import com.benardmathu.hfms.data.user.model.User;

import java.util.ArrayList;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class UserDto {
    @SerializedName(USER)
    private User user;
    @SerializedName(INCOME)
    private Income income;
    @SerializedName(DbEnvironment.HOUSEHOLD_TB_NAME)
    private ArrayList<Household> households;
    @SerializedName(ACCOUNT_STATUS_UPDATE)
    private AccountStatus accountStatus = null;
    @SerializedName(RELATION)
    private ArrayList<UserHouseholdRel> userHouseholdRels;
    @SerializedName(HOUSEHOLD_MEMBERS)
    private ArrayList<User> members;
    @SerializedName(DbEnvironment.TRANSACTION_TB_NAME)
    private ArrayList<Transaction> transactions;
    @SerializedName(DbEnvironment.ON_UPDATE_INCOME)
    private OnInComeChange onIncomeChange;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public Income getIncome() {
        return income;
    }

    public void setHouseholds(ArrayList<Household> households) {
        this.households = households;
    }

    public ArrayList<Household> getHouseholds() {
        return households;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public ArrayList<UserHouseholdRel> getUserHouseholdRels() {
        return userHouseholdRels;
    }

    public void setUserHouseholdRels(ArrayList<UserHouseholdRel> userHouseholdRels) {
        this.userHouseholdRels = userHouseholdRels;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setOnIncomeChange(OnInComeChange onIncomeChange) {
        this.onIncomeChange = onIncomeChange;
    }
    
    public void setIncomeChange(OnInComeChange onInComeChange) {
        this.onIncomeChange = onInComeChange;
    }
}

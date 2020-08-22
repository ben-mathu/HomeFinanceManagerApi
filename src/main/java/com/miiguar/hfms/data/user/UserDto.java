package com.miiguar.hfms.data.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.budget.model.Budget;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.user.model.User;

import java.util.ArrayList;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.utils.Constants.*;

/**
 * @author bernard
 */
public class UserDto {
    @SerializedName(USER)
    private User user;
    @SerializedName(INCOME)
    private Income income;
    @SerializedName(HOUSEHOLD_TB_NAME)
    private ArrayList<Household> households;
    @SerializedName(ACCOUNT_STATUS_UPDATE)
    private AccountStatus accountStatus = null;
    @SerializedName(RELATION)
    private ArrayList<UserHouseholdRel> userHouseholdRels;
    @SerializedName(HOUSEHOLD_MEMBERS)
    private ArrayList<User> members;
    private ArrayList<Budget> budgets;

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

    public void setBudgets(ArrayList<Budget> budgets) {
        this.budgets = budgets;
    }

    public ArrayList<Budget> getBudgets() {
        return budgets;
    }
}

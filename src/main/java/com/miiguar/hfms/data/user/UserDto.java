package com.miiguar.hfms.data.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.assets.model.Assets;
import com.miiguar.hfms.data.status.AccountStatus;
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
    private Assets income;
    @SerializedName(HOUSEHOLD_TB_NAME)
    private ArrayList<Household> households;
    @SerializedName(ACCOUNT_STATUS_UPDATE)
    private AccountStatus accountStatus = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIncome(Assets income) {
        this.income = income;
    }

    public Assets getIncome() {
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
}

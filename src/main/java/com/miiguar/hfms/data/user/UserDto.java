package com.miiguar.hfms.data.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.user.model.User;

import java.util.ArrayList;

/**
 * @author bernard
 */
public class UserDto {
    @SerializedName("user")
    private User user;
    @SerializedName("income")
    private Income income;
    private ArrayList<Household> households;

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
}

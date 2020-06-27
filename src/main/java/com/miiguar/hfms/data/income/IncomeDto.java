package com.miiguar.hfms.data.income;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.income.model.Income;

/**
 * @author bernard
 */
public class IncomeDto {
    @SerializedName("income")
    private Income income;

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }
}

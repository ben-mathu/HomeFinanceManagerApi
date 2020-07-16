package com.miiguar.hfms.data.expense;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.expense.model.Expense;

import static com.miiguar.hfms.utils.Constants.EXPENSE;

/**
 * @author bernard
 */
public class ExpenseDto {
    @SerializedName(EXPENSE)
    private Expense expense;

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }
}

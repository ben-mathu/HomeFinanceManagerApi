package com.benardmathu.hfms.data.expense;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.expense.model.Expense;
import static com.benardmathu.hfms.utils.Constants.EXPENSE;

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

package com.benardmathu.hfms.data.budget;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.budget.model.Budget;

/**
 * @author bernard
 */
public class BudgetDto {
    @SerializedName("bugdet")
    private Budget budget;

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}

package com.benardmathu.hfms.data.budget.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Column;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.PrimaryKey;
import com.benardmathu.hfms.init.Table;
import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.DbEnvironment.CREATED_AT;

/**
 * @author bernard
 */
@Table(tableName = BUDGET_TB_NAME,
        constraint = @Constraint(
                name = FK_BUDGETS_HOUSEHOLD_ID,
                tableName = HOUSEHOLD_TB_NAME,
                columnName = HOUSEHOLD_ID
        ))
public class Budget {
    @SerializedName(BUDGET_ID)
    @PrimaryKey(columnName = BUDGET_ID)
    private String budgetId = "";

    @SerializedName(BUDGET_AMOUNT)
    @Column(columnName = BUDGET_AMOUNT)
    private String budgetAmount = "";

    @SerializedName(BUDGET_DESC)
    @Column(columnName = BUDGET_DESC, characterLength = 255, notNull = false)
    private String budgetDesc = "";

    @SerializedName(HOUSEHOLD_ID)
    @Column(columnName = HOUSEHOLD_ID, notNull = false)
    private String householdId = "";

    @SerializedName(CREATED_AT)
    @Column(columnName = CREATED_AT, characterLength = 25)
    private String createdAt = "";

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(String budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getBudgetDesc() {
        return budgetDesc;
    }

    public void setBudgetDesc(String budgetDesc) {
        this.budgetDesc = budgetDesc;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

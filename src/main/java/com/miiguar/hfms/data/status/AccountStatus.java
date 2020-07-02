package com.miiguar.hfms.data.status;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(tableName = ACCOUNT_STATUS_TB_NAME,
        constraint = {@Constraint(
                name = FK_TB_USERID_REF_USERS,
                columnName = USER_ID,
                tableName = USERS_TB_NAME
        )}
)
public class AccountStatus {
    @SerializedName(USER_ID)
    @Column(columnName = USER_ID)
    private String userId = "";

    @SerializedName(ACCOUNT_STATUS)
    @Column(columnName = ACCOUNT_STATUS, notNull = false, characterLength = 255)
    private String accountStatus = "";

    @SerializedName(INCOME_STATUS)
    @Column(columnName = INCOME_STATUS, notNull = false, characterLength = 255)
    private String incomeStatus = "";

    @SerializedName(GROCERY_STATUS)
    @Column(columnName = GROCERY_STATUS, notNull = false, characterLength = 255)
    private String groceryStatus = "";

    @SerializedName(EXPENSES_STATUS)
    @Column(columnName = EXPENSES_STATUS, notNull = false, characterLength = 255)
    private String expensesStatus = "";

    @SerializedName(HOUSEHOLD_STATUS)
    @Column(columnName = HOUSEHOLD_STATUS, notNull = false, characterLength = 255)
    private String householdStatus = "";

    @SerializedName(COMPLETE_AT)
    @Column(columnName = COMPLETE_AT, notNull = false, characterLength = 125)
    private String reminder = "";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIncomeStatus() {
        return incomeStatus;
    }

    public void setIncomeStatus(String incomeStatus) {
        this.incomeStatus = incomeStatus;
    }

    public String getGroceryStatus() {
        return groceryStatus;
    }

    public void setGroceryStatus(String groceryStatus) {
        this.groceryStatus = groceryStatus;
    }

    public String getExpensesStatus() {
        return expensesStatus;
    }

    public void setExpensesStatus(String expensesStatus) {
        this.expensesStatus = expensesStatus;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setHouseholdStatus(String statusStr) {
        this.householdStatus = statusStr;
    }

    public String getHouseholdStatus() {
        return householdStatus;
    }
}

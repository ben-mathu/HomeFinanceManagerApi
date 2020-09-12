package com.benardmathu.hfms.data.income.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Column;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.PrimaryKey;
import com.benardmathu.hfms.init.Table;
import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(tableName = INCOME_TB_NAME,
        constraint = {@Constraint(
                tableName = USERS_TB_NAME,
                name = FK_INCOME_USER_ID,
                columnName = USER_ID
        )}
)
public class Income {
    @SerializedName(INCOME_ID)
    @PrimaryKey(columnName = INCOME_ID)
    private String incomeId = "";

    @SerializedName(AMOUNT)
    @Column(columnName = AMOUNT)
    private double amount = 0;

    @SerializedName(INCOME_TYPE)
    @Column(columnName = INCOME_TYPE, unique = true)
    private String accountType = "";

    @SerializedName(USER_ID)
    @Column(columnName = USER_ID)
    private String userId = "";
    
    @SerializedName(SCHEDULED_FOR)
    @Column(columnName = SCHEDULED_FOR, characterLength = 25)
    private String schedule = "";

    @SerializedName(CREATED_AT)
    @Column(columnName = CREATED_AT, characterLength = 25)
    private String createdAt = "";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(String incomeId) {
        this.incomeId = incomeId;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSchedule() {
        return schedule;
    }
}

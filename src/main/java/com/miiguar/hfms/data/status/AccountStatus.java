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
                name = FK_ACCOUNT_STATUS_USER_ID,
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

    @SerializedName(ENVELOPE_STATUS)
    @Column(columnName = ENVELOPE_STATUS, notNull = false, characterLength = 255)
    private String envelopeStatus = "";

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

    public String getEnvelopeStatus() {
        return envelopeStatus;
    }

    public void setEnvelopeStatus(String envelopeStatus) {
        this.envelopeStatus = envelopeStatus;
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

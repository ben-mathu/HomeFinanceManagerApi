package com.benardmathu.hfms.data.status;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbEnvironment.ACCOUNT_STATUS_TB_NAME)
public class AccountStatus {
    @Id
    private Long id;
    
    @SerializedName(DbEnvironment.USER_ID)
    @Column(name = DbEnvironment.USER_ID)
    private String userId = "";

    @SerializedName(DbEnvironment.ACCOUNT_STATUS)
    @Column(name = DbEnvironment.ACCOUNT_STATUS, nullable = false, length = 255)
    private String accountStatus = "";

    @SerializedName(DbEnvironment.INCOME_STATUS)
    @Column(name = DbEnvironment.INCOME_STATUS, nullable = false, length = 255)
    private String incomeStatus = "";

    @SerializedName(DbEnvironment.JAR_STATUS)
    @Column(name = DbEnvironment.JAR_STATUS, nullable = false, length = 255)
    private String jarStatus = "";

    @SerializedName(DbEnvironment.HOUSEHOLD_STATUS)
    @Column(name = DbEnvironment.HOUSEHOLD_STATUS, nullable = false, length = 255)
    private String householdStatus = "";

    @SerializedName(DbEnvironment.COMPLETE_AT)
    @Column(name = DbEnvironment.COMPLETE_AT, nullable = false, length = 125)
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

    public String getJarStatus() {
        return jarStatus;
    }

    public void setJarStatus(String jarStatus) {
        this.jarStatus = jarStatus;
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

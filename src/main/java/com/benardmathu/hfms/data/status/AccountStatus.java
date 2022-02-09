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
@Table(name = ACCOUNT_STATUS_TB_NAME)
public class AccountStatus {
    @Id
    private Long id;
    
    @SerializedName(USER_ID)
    @Column(name = USER_ID)
    private String userId = "";

    @SerializedName(ACCOUNT_STATUS)
    @Column(name = ACCOUNT_STATUS, nullable = false, length = 255)
    private String accountStatus = "";

    @SerializedName(INCOME_STATUS)
    @Column(name = INCOME_STATUS, nullable = false, length = 255)
    private String incomeStatus = "";

    @SerializedName(JAR_STATUS)
    @Column(name = JAR_STATUS, nullable = false, length = 255)
    private String jarStatus = "";

    @SerializedName(HOUSEHOLD_STATUS)
    @Column(name = HOUSEHOLD_STATUS, nullable = false, length = 255)
    private String householdStatus = "";

    @SerializedName(COMPLETE_AT)
    @Column(name = COMPLETE_AT, nullable = false, length = 125)
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

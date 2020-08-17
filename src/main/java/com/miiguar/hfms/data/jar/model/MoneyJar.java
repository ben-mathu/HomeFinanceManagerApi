package com.miiguar.hfms.data.jar.model;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.PrimaryKey;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(
        tableName = MONEY_JAR_TB_NAME,
        constraint = {@Constraint(
                name = FK_MONEY_JAR_HOUSEHOLD_ID,
                columnName = HOUSEHOLD_ID,
                tableName = HOUSEHOLD_TB_NAME
        )}
)
public class MoneyJar {
    @PrimaryKey(columnName = MONEY_JAR_ID)
    @SerializedName(MONEY_JAR_ID)
    private String moneyJarId = "";

    @SerializedName(MONEY_JAR_NAME)
    @Column(columnName = MONEY_JAR_NAME, characterLength = 45, unique = true)
    private String name = "";

    @SerializedName(CATEGORY)
    @Column(columnName = CATEGORY, characterLength = 45)
    private String category = "";

    @SerializedName(TOTAL_AMOUNT)
    @Column(columnName = TOTAL_AMOUNT)
    private double totalAmount = 0;

    @SerializedName(CREATED_AT)
    @Column(columnName = CREATED_AT, characterLength = 45)
    private String createdAt = "";

    @SerializedName(SCHEDULED_FOR)
    @Column(columnName = SCHEDULED_FOR, characterLength = 45)
    private String scheduledFor = "";

    @SerializedName(SCHEDULED_TYPE)
    @Column(columnName = SCHEDULED_TYPE, characterLength = 45)
    private String scheduleType;

    @SerializedName(HOUSEHOLD_ID)
    @Column(columnName = HOUSEHOLD_ID)
    private String householdId;

    public String getMoneyJarId() {
        return moneyJarId;
    }

    public void setMoneyJarId(String moneyJarId) {
        this.moneyJarId = moneyJarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(String scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }
}

package com.miiguar.hfms.data.tablerelationships.schedulejarrel;

import com.google.gson.annotations.SerializedName;
import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.Table;

/**
 * Normalize schedule to encourage redundancies
 * @author bernard
 */
@Table(tableName = MONEY_JAR_SCHEDULE_REL_TB,
        constraint = {@Constraint(
                name = FK_MONEY_JAR_SCHEDULE_JAR_ID,
                columnName = MONEY_JAR_ID,
                tableName = MONEY_JAR_TB_NAME
        )}
)
public class JarScheduleDateRel {
    @Column(columnName = HOUSEHOLD_ID)
    private String householdId = "";
    
    @Column(columnName = MONEY_JAR_ID)
    private String jarId = "";
    
    @Column(columnName = JAR_SCHEDULE_DATE, characterLength = 45)
    private String scheduleDate = "";
    
    @Column(columnName = JAR_STATUS)
    private boolean jarStatus = false;
    
    @Column(columnName = PAYMENT_STATUS)
    private boolean paymentStatus = false;

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }

    public String getJarId() {
        return jarId;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setJarStatus(boolean jarStatus) {
        this.jarStatus = jarStatus;
    }

    public boolean isJarStatus() {
        return jarStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }
}
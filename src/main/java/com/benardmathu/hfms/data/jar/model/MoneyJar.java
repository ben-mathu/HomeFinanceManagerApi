package com.benardmathu.hfms.data.jar.model;

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
@Table(name = MONEY_JAR_TB_NAME)
public class MoneyJar {
    @SerializedName(MONEY_JAR_ID)
    @Id
    private Long moneyJarId;

    @SerializedName(MONEY_EXPENSE_TYPE)
    @Column(name = MONEY_EXPENSE_TYPE, length = 45)
    private String name = "";

    @SerializedName(CATEGORY)
    @Column(name = CATEGORY, length = 45)
    private String category = "";

    @SerializedName(TOTAL_AMOUNT)
    @Column(name = TOTAL_AMOUNT)
    private double totalAmount = 0;

    @SerializedName(CREATED_AT)
    @Column(name = CREATED_AT, length = 45)
    private String createdAt = "";

    @SerializedName(SCHEDULED_FOR)
    @Column(name = SCHEDULED_FOR, length = 45)
    private String scheduledFor = "";

    @SerializedName(SCHEDULED_TYPE)
    @Column(name = SCHEDULED_TYPE, length = 45)
    private String scheduleType;

    @SerializedName(HOUSEHOLD_ID)
    @Column(name = HOUSEHOLD_ID)
    private String householdId = "";

    @SerializedName(JAR_STATUS)
    @Column(name = JAR_STATUS)
    private boolean jarStatus = false;
    
    @SerializedName(PAYMENT_STATUS)
    @Column(name = PAYMENT_STATUS)
    private boolean paymentStatus = false;
}

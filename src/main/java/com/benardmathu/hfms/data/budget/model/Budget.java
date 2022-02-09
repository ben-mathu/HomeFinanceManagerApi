package com.benardmathu.hfms.data.budget.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.DbEnvironment.CREATED_AT;

/**
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Budget {
    @SerializedName(BUDGET_ID)
    @Id
    private Long budgetId;

    @SerializedName(BUDGET_AMOUNT)
    @Column(name = BUDGET_AMOUNT)
    private String budgetAmount = "";

    @SerializedName(BUDGET_DESC)
    @Column(name = BUDGET_DESC, length = 255, nullable = false)
    private String budgetDesc = "";

    @SerializedName(HOUSEHOLD_ID)
    @Column(name = HOUSEHOLD_ID, nullable = false)
    private String householdId = "";

    @SerializedName(CREATED_AT)
    @Column(name = CREATED_AT, length = 25)
    private String createdAt = "";
}

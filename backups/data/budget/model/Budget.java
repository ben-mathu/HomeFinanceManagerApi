package com.benardmathu.hfms.data.budget.model;

import com.benardmathu.hfms.data.household.model.Household;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
@Table(name = DbEnvironment.BUDGET_TB_NAME)
public class Budget {
    @SerializedName(DbEnvironment.BUDGET_ID)
    @Id
    private Long budgetId;

    @SerializedName(DbEnvironment.BUDGET_AMOUNT)
    @Column(name = DbEnvironment.BUDGET_AMOUNT)
    private String budgetAmount = "";

    @SerializedName(DbEnvironment.BUDGET_DESC)
    @Column(name = DbEnvironment.BUDGET_DESC, length = 255, nullable = false)
    private String budgetDesc = "";

    @SerializedName("household")
    @ManyToOne(optional = false)
    @JoinColumn(name = DbEnvironment.HOUSEHOLD_ID, referencedColumnName = DbEnvironment.HOUSEHOLD_ID)
    private Household household;

    @SerializedName(DbEnvironment.CREATED_AT)
    @Column(name = DbEnvironment.CREATED_AT, length = 25)
    private String createdAt = "";
}

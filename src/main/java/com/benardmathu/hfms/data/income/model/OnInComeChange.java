package com.benardmathu.hfms.data.income.model;

import static com.benardmathu.hfms.data.utils.DbEnvironment.AMOUNT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.CREATED_AT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.FK_INCOME_CHANGE_INCOME_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.INCOME_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.INCOME_TB_NAME;
import static com.benardmathu.hfms.data.utils.DbEnvironment.ON_UPDATE_INCOME;
import com.benardmathu.hfms.init.Column;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.Table;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author bernard
 */
@Table(tableName = ON_UPDATE_INCOME,
        constraint = {@Constraint(
                name = FK_INCOME_CHANGE_INCOME_ID,
                tableName = INCOME_TB_NAME,
                columnName = INCOME_ID
        )}
)
public class OnInComeChange {
    @SerializedName(AMOUNT)
    @Column(columnName = AMOUNT)
    private double amount;
    @SerializedName(INCOME_ID)
    @Column(columnName = INCOME_ID, characterLength = 45)
    private String incomeId;
    @SerializedName(CREATED_AT)
    @Column(columnName = CREATED_AT, characterLength = 255)
    private String createdAt;

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setIncomeId(String incomeId) {
        this.incomeId = incomeId;
    }

    public String getIncomeId() {
        return incomeId;
    }
}

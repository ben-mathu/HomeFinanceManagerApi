package com.benardmathu.hfms.data.income;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.status.Report;
import static com.benardmathu.hfms.utils.Constants.INCOME;
import static com.benardmathu.hfms.utils.Constants.LAST_INCOME;
import static com.benardmathu.hfms.utils.Constants.REPORT;

/**
 * @author bernard
 */
public class IncomeDto {
    @SerializedName(INCOME)
    private Income income;
    @SerializedName(REPORT)
    private Report report;
    @SerializedName(LAST_INCOME)
    private OnInComeChange onIncomeChange;

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

    public void setOnIncomeChange(OnInComeChange onInComeChange) {
        this.onIncomeChange = onInComeChange;
    }

    public OnInComeChange getOnIncomeChange() {
        return onIncomeChange;
    }
}

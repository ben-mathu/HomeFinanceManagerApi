package com.benardmathu.hfms.data.jar;

import com.benardmathu.hfms.data.income.model.Income;
import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.status.Report;
import static com.benardmathu.hfms.utils.Constants.INCOME;

import java.util.ArrayList;

import static com.benardmathu.hfms.utils.Constants.JAR_ELEMENTS;

/**
 * @author bernard
 */
public class MoneyJarsDto {
    @SerializedName(JAR_ELEMENTS)
    private ArrayList<MoneyJarDto> jarDto;
    @SerializedName("report")
    private Report report;
    @SerializedName(INCOME)
    private Income income;

    public ArrayList<MoneyJarDto> getJarDto() {
        return jarDto;
    }

    public void setJarDto(ArrayList<MoneyJarDto> jarDto) {
        this.jarDto = jarDto;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public Income getIncome() {
        return income;
    }
}

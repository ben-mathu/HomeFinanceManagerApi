/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benardmathu.hfms.data.report;

import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bernard
 */
public class ReportDto {
    @SerializedName("income")
    private double income = 0;
    @SerializedName("expenses")
    private List<MoneyJar> moneyJars = new ArrayList<>();
    @SerializedName("expense_total_amount")
    private double totalExpenseAmount = 0;
    @SerializedName("net_income")
    private double netIncome;
    @SerializedName("tax")
    private double tax = 0;
    @SerializedName("months_difference")
    private int monthsRange;
    @SerializedName("date_ending")
    private String dateEnding = "";
    @SerializedName("years_range")
    private String yearRange;
    @SerializedName("income_after_tax")
    private double incomeAfterTax;
    @SerializedName("personal_relief")
    private double personalRelief;

    public void setIncome(double income) {
        this.income = income;
    }

    public double getIncome() {
        return income;
    }

    public void setMoneyJars(List<MoneyJar> moneyJars) {
        this.moneyJars = moneyJars;
    }

    public List<MoneyJar> getMoneyJars() {
        return moneyJars;
    }

    public void setTotalExpenseAmount(double totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public double getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setNetIncome(double netIncome) {
        this.netIncome = netIncome;
    }

    public double getNetIncome() {
        return netIncome;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTax() {
        return tax;
    }

    public int getMonthsRange() {
        return monthsRange;
    }
    
    public void setMonthsRange(int monthsRange) {
        this.monthsRange = monthsRange;
    }

    public void setDateEnding(String dateEnding) {
        this.dateEnding = dateEnding;
    }

    public String getDateEnding() {
        return dateEnding;
    }

    public void setYearRange(String yearRange) {
        this.yearRange = yearRange;
    }

    public String getYearRange() {
        return yearRange;
    }

    public void setIncomeAfterTax(double incomeAfterTax) {
        this.incomeAfterTax = incomeAfterTax;
    }

    public double getIncomeAfterTax() {
        return incomeAfterTax;
    }

    public void setPersonalRelief(double personalRelief) {
        this.personalRelief = personalRelief;
    }

    public double getPersonalRelief() {
        return personalRelief;
    }
}

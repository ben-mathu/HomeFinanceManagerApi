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
    double income = 0;
    @SerializedName("expenses")
    private List<MoneyJar> moneyJars = new ArrayList<>();

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
    
    
}

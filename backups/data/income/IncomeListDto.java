package com.benardmathu.hfms.data.income;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.income.model.Income;

import java.util.ArrayList;

/**
 * @author bernard
 */
public class IncomeListDto {
    @SerializedName("income_list")
    private ArrayList<Income> list;

    public ArrayList<Income> getList() {
        return list;
    }

    public void setList(ArrayList<Income> list) {
        this.list = list;
    }
}

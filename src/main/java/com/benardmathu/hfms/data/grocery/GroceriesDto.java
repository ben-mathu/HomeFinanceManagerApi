package com.benardmathu.hfms.data.grocery;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.grocery.model.Grocery;

import java.util.ArrayList;

import static com.benardmathu.hfms.data.utils.DbEnvironment.GROCERIES_TB_NAME;

/**
 * @author bernard
 */
public class GroceriesDto {
    @SerializedName(GROCERIES_TB_NAME)
    private ArrayList<Grocery> groceries;

    public ArrayList<Grocery> getGroceries() {
        return groceries;
    }

    public void setGroceries(ArrayList<Grocery> groceries) {
        this.groceries = groceries;
    }
}

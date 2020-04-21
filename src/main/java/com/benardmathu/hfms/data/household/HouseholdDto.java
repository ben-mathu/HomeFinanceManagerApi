package com.benardmathu.hfms.data.household;

import com.benardmathu.hfms.data.household.model.Household;
import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class HouseholdDto {
    @SerializedName("Household")
    private Household household;

    public void setHousehold(Household household) {
        this.household = household;
    }

    public Household getHousehold() {
        return household;
    }
}

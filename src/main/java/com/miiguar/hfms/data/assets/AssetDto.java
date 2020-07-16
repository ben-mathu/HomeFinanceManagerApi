package com.miiguar.hfms.data.assets;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.assets.model.Assets;

import static com.miiguar.hfms.utils.Constants.INCOME;

/**
 * @author bernard
 */
public class AssetDto {
    @SerializedName(INCOME)
    private Assets income;

    public Assets getIncome() {
        return income;
    }

    public void setIncome(Assets income) {
        this.income = income;
    }
}

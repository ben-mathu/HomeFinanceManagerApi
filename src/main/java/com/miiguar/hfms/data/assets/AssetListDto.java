package com.miiguar.hfms.data.assets;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.assets.model.Assets;

import java.util.ArrayList;

/**
 * @author bernard
 */
public class AssetListDto {
    @SerializedName("income_list")
    private ArrayList<Assets> list;

    public ArrayList<Assets> getList() {
        return list;
    }

    public void setList(ArrayList<Assets> list) {
        this.list = list;
    }
}

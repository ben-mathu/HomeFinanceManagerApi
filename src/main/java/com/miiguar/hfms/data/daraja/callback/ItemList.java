package com.miiguar.hfms.data.daraja.callback;

import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class ItemList {
    @SerializedName("Name")
    private String name = "";
    @SerializedName("Value")
    private String value = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

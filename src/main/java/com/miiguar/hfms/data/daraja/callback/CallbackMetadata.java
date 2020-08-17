package com.miiguar.hfms.data.daraja.callback;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.miiguar.hfms.utils.Constants.LnmoResponseFields.ITEM;

/**
 * @author bernard
 */
public class CallbackMetadata {
    @SerializedName(ITEM)
    private List<ItemList> items;

    public List<ItemList> getItems() {
        return items;
    }

    public void setItems(List<ItemList> items) {
        this.items = items;
    }
}

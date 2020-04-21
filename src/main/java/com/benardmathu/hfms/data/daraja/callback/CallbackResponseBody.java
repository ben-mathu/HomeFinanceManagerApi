package com.benardmathu.hfms.data.daraja.callback;

import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class CallbackResponseBody {
    @SerializedName("stkCallback")
    private StkCallback callback;

    public StkCallback getCallback() {
        return callback;
    }

    public void setCallback(StkCallback callback) {
        this.callback = callback;
    }
}

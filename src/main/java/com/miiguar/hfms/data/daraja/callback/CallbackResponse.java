package com.miiguar.hfms.data.daraja.callback;

import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class CallbackResponse {
    @SerializedName("Body")
    private CallbackResponseBody body;

    public CallbackResponseBody getBody() {
        return body;
    }

    public void setBody(CallbackResponseBody body) {
        this.body = body;
    }
}

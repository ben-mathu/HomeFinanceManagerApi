package com.miiguar.hfms.data.status;

import com.google.gson.annotations.SerializedName;

public class MessageReport {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public MessageReport(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

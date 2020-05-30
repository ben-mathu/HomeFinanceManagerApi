package com.miiguar.hfms.data.status;

import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("status")
    private int status = 200;
    @SerializedName("message")
    private String message = "";
    @SerializedName("token")
    private String token = "";

    public Report() {
        throw new UnsupportedOperationException("You should not do this...");
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

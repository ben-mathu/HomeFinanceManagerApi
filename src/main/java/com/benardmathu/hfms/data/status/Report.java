package com.benardmathu.hfms.data.status;

import com.google.gson.annotations.SerializedName;

import static com.benardmathu.hfms.utils.Constants.SUBJECT;

public class Report {
    @SerializedName("status")
    private int status = 200;
    @SerializedName("message")
    private String message = "";
    @SerializedName("token")
    private String token = "";
    @SerializedName(SUBJECT)
    private String subject = "";

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

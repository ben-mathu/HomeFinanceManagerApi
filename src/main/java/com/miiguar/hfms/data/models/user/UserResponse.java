package com.miiguar.hfms.data.models.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;

/**
 * @author bernard
 */
public class UserResponse {
    @SerializedName("response")
    private Report report;
    @SerializedName("user")
    private User user;

    public UserResponse() {
        super();
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

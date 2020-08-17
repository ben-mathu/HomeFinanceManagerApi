package com.miiguar.hfms.data.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;

/**
 * @author bernard
 */
public class UserResponse {
    @SerializedName("report")
    private Report report;
    @SerializedName("user")
    private User user;
    @SerializedName("household")
    private Household household;

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

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }
}

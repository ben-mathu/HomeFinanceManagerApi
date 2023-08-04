/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benardmathu.hfms.data.user;

import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.user.model.User;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author bernard
 */
public class Members {
    @SerializedName("household_id")
    private Long householdId;
    
    @SerializedName("household_name")
    private String householdName;
    
    @SerializedName("members")
    private List<User> users;
    
    @SerializedName("report")
    private Report report;

    public void setHouseholdId(Long householdId) {
        this.householdId = householdId;
    }

    public Long getHouseholdId() {
        return householdId;
    }
    
    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public String getHouseholdName() {
        return householdName;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }
}

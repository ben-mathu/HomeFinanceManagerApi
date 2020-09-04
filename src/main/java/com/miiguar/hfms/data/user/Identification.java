package com.miiguar.hfms.data.user;

import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;

/**
 * @author bernard
 */
public class Identification {
    private String code = "";
    private Report report;
    private User user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}

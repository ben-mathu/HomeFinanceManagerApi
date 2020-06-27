package com.miiguar.hfms.data.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.status.Report;

/**
 * @author bernard
 */
public class ConfirmationResponse {
    @SerializedName("report")
    private Report report;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}

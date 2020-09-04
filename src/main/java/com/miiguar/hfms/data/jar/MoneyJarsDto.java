package com.miiguar.hfms.data.jar;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.status.Report;

import java.util.ArrayList;

import static com.miiguar.hfms.utils.Constants.JAR_ELEMENTS;

/**
 * @author bernard
 */
public class MoneyJarsDto {
    @SerializedName(JAR_ELEMENTS)
    private ArrayList<MoneyJarDto> jarDto;
    @SerializedName("report")
    private Report report;

    public ArrayList<MoneyJarDto> getJarDto() {
        return jarDto;
    }

    public void setJarDto(ArrayList<MoneyJarDto> jarDto) {
        this.jarDto = jarDto;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}

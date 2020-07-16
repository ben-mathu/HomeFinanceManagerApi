package com.miiguar.hfms.data.container;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.container.ContainerDto;
import com.miiguar.hfms.data.status.Report;

import java.util.ArrayList;

import static com.miiguar.hfms.utils.Constants.ENVELOPE_ELEMENTS;

/**
 * @author bernard
 */
public class ContainersDto {
    @SerializedName(ENVELOPE_ELEMENTS)
    private ArrayList<ContainerDto> envelopeDto;
    @SerializedName("report")
    private Report report;

    public ArrayList<ContainerDto> getEnvelopeDto() {
        return envelopeDto;
    }

    public void setEnvelopeDto(ArrayList<ContainerDto> envelopeDto) {
        this.envelopeDto = envelopeDto;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}

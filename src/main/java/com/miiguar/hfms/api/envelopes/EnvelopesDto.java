package com.miiguar.hfms.api.envelopes;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.envelope.EnvelopeDto;
import com.miiguar.hfms.data.status.Report;

import java.util.ArrayList;

import static com.miiguar.hfms.utils.Constants.ENVELOPE_ELEMENTS;

/**
 * @author bernard
 */
public class EnvelopesDto {
    @SerializedName(ENVELOPE_ELEMENTS)
    private ArrayList<EnvelopeDto> envelopeDto;
    @SerializedName("report")
    private Report report;

    public ArrayList<EnvelopeDto> getEnvelopeDto() {
        return envelopeDto;
    }

    public void setEnvelopeDto(ArrayList<EnvelopeDto> envelopeDto) {
        this.envelopeDto = envelopeDto;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}

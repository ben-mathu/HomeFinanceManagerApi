package com.benardmathu.hfms.data.report;

import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author bernard
 */
public class ReportRequest {
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;
    @SerializedName(USER_ID)
    private Long userId;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}

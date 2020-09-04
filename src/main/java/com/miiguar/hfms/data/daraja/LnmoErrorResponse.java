package com.miiguar.hfms.data.daraja;

import com.google.gson.annotations.SerializedName;

import static com.miiguar.hfms.utils.Constants.LnmoRequestFields.*;

/**
 * @author bernard
 */
public class LnmoErrorResponse {
    @SerializedName(REQUEST_ID)
    private String requestId = "";
    @SerializedName(ERROR_CODE)
    private String errorCode = "";
    @SerializedName(ERROR_MESSAGE)
    private String errorMessage = "";

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

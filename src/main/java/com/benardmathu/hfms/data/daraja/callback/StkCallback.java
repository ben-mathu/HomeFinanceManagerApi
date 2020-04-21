package com.benardmathu.hfms.data.daraja.callback;

import com.google.gson.annotations.SerializedName;

import static com.benardmathu.hfms.utils.Constants.LnmoResponseFields.*;

/**
 * @author bernard
 */
public class StkCallback {
    @SerializedName(MERCHANT_REQUEST_ID)
    private String merchantRequestId = "";
    @SerializedName(CHECKOUT_REQ_ID)
    private String checkoutRequestID = "";
    @SerializedName(RESULT_CODE)
    private String resultCode = "";
    @SerializedName(RESULT_DESC)
    private String resultDesc = "";
    @SerializedName(CALLBACK_METADATA)
    private CallbackMetadata metadata;

    public String getMerchantRequestId() {
        return merchantRequestId;
    }

    public void setMerchantRequestId(String merchantRequestId) {
        this.merchantRequestId = merchantRequestId;
    }

    public String getCheckoutRequestID() {
        return checkoutRequestID;
    }

    public void setCheckoutRequestID(String checkoutRequestID) {
        this.checkoutRequestID = checkoutRequestID;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public CallbackMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CallbackMetadata metadata) {
        this.metadata = metadata;
    }
}

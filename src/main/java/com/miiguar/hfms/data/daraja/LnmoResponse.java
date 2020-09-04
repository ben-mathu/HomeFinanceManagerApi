package com.miiguar.hfms.data.daraja;

import com.google.gson.annotations.SerializedName;

import static com.miiguar.hfms.utils.Constants.LnmoResponseFields.*;

/**
 * @author bernard
 */
public class LnmoResponse {
    @SerializedName(MERCHANT_REQUEST_ID)
    private String merchantReqId = "";
    @SerializedName(CHECKOUT_REQ_ID)
    private String checkoutReqId = "";
    @SerializedName(RESPONSE_CODE)
    private String respCode = "";
    @SerializedName(RESPONSE_DESCRIPTION)
    private String respDesc = "";
    @SerializedName(CUSTOMER_MESSAGE)
    private String customerMessage = "";

    public String getMerchantReqId() {
        return merchantReqId;
    }

    public void setMerchantReqId(String merchantReqId) {
        this.merchantReqId = merchantReqId;
    }

    public String getCheckoutReqId() {
        return checkoutReqId;
    }

    public void setCheckoutReqId(String checkoutReqId) {
        this.checkoutReqId = checkoutReqId;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }
}

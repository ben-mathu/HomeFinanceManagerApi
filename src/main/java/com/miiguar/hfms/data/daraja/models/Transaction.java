package com.miiguar.hfms.data.daraja.models;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.PrimaryKey;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(tableName = TRANSACTION_TB_NAME)
public class Transaction {
    @SerializedName(CHECKOUT_REQ_ID)
    @PrimaryKey(columnName = CHECKOUT_REQ_ID, characterLength = 45)
    private String checkoutReqId = "";

    @SerializedName(MERCHANT_REQUEST_ID)
    @Column(columnName = MERCHANT_REQUEST_ID, characterLength = 45, unique = true, notNull = false)
    private String merchantReqId;

    @SerializedName(RESULT_CODE)
    @Column(columnName = RESULT_CODE, notNull = false)
    private String resultCode;

    @SerializedName(RESULT_DESC)
    @Column(columnName = RESULT_DESC, characterLength = 255, notNull = false)
    private String resultDesc;

    @SerializedName(CALLBACK_METADATA)
    @Column(columnName = CALLBACK_METADATA, characterLength = 1024, notNull = false)
    private String callbackMetadata;

    @SerializedName(TRANSACTION_STATUS)
    @Column(columnName = TRANSACTION_STATUS, characterLength = 1, notNull = false)
    private boolean transactionComplete = false;

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

    public String getCallbackMetadata() {
        return callbackMetadata;
    }

    public void setCallbackMetadata(String callbackMetadata) {
        this.callbackMetadata = callbackMetadata;
    }

    public boolean isTransactionComplete() {
        return transactionComplete;
    }

    public void setTransactionComplete(boolean transactionComplete) {
        this.transactionComplete = transactionComplete;
    }
}

package com.miiguar.hfms.data.daraja;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.config.ConfigureApp;

import java.util.Properties;

import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.BASE_URL;
import static com.miiguar.hfms.utils.Constants.LnmoRequestFields.*;

/**
 * @author bernard
 */
public class LnmoRequest {
    @SerializedName(SHORT_CODE)
    private String businessShortCode = "";
    @SerializedName(PASSWORD)
    private String password = "";
    @SerializedName(TIMESTAMP)
    private String timestamp = "";
    @SerializedName(TRANSACTION_TYPE)
    private String transactionType = "";
    @SerializedName(AMOUNT)
    private String amount = "";
    @SerializedName(PARTY_A)
    private String payer = "";
    @SerializedName(PARTY_B)
    private String payee = "";
    @SerializedName(PHONE_NUMBER)
    private String phoneNumber = "";
    @SerializedName(CALLBACK_URL)
    private String callbackUrl = "";
    @SerializedName(ACCOUNT_REF)
    private String accountRef = "";
    @SerializedName(TRANSACTION_DESC)
    private String transactionDesc = "";

    public String getBusinessShortCode() {
        return businessShortCode;
    }

    public void setBusinessShortCode(String businessShortCode) {
        this.businessShortCode = businessShortCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        ConfigureApp app = new ConfigureApp();
        Properties properties = app.getProperties();
        this.callbackUrl = BASE_URL + API + callbackUrl;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }
}

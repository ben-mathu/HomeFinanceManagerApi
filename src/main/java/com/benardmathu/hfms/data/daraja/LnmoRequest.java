package com.benardmathu.hfms.data.daraja;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.config.ConfigureApp;

import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.MONEY_JAR_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.BASE_URL;
import static com.benardmathu.hfms.utils.Constants.LnmoRequestFields.*;

/**
 * @author bernard
 */
public class LnmoRequest {
    @SerializedName(SHORT_CODE)
    @Expose
    private String businessShortCode = "";
    @SerializedName(PASSWORD)
    @Expose
    private String password = "";
    @SerializedName(TIMESTAMP)
    @Expose
    private String timestamp = "";
    @SerializedName(TRANSACTION_TYPE)
    @Expose
    private String transactionType = "";
    @SerializedName(AMOUNT)
    @Expose
    private String amount = "";
    @SerializedName(PARTY_A)
    @Expose
    private String payer = "";
    @SerializedName(PARTY_B)
    @Expose
    private String payee = "";
    @SerializedName(PHONE_NUMBER)
    @Expose
    private String phoneNumber = "";
    @SerializedName(CALLBACK_URL)
    @Expose
    private String callbackUrl = "";
    @SerializedName(ACCOUNT_REF)
    @Expose
    private String accountRef = "";
    @SerializedName(TRANSACTION_DESC)
    @Expose
    private String transactionDesc = "";
    @SerializedName(MONEY_JAR_ID)
    private String jarId;
    @SerializedName(USER_ID)
    private String userId;

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

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }

    public String getJarId() {
        return jarId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

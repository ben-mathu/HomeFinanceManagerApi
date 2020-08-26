package com.miiguar.hfms.data.daraja.models;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.PrimaryKey;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import com.miiguar.hfms.init.Constraint;

/**
 * @author bernard
 */
@Table(tableName = TRANSACTION_TB_NAME,
        constraint = {@Constraint(
                name = FK_TRANSACTIONS_JAR_ID,
                columnName = MONEY_JAR_ID,
                tableName = MONEY_JAR_TB_NAME
        )}
)
public class Transaction {
    @SerializedName(TRANSACTION_ID)
    @PrimaryKey(columnName = TRANSACTION_ID)
    private String id;
    
    @SerializedName(TRANSACTION_DESCRIPTION)
    @Column(columnName = TRANSACTION_DESCRIPTION)
    private String transactionDesc;
    
    @SerializedName(PAYMENT_DETAILS)
    @Column(columnName = PAYMENT_DETAILS, characterLength = 255, notNull = false)
    private String paymentDetails;
    
    @SerializedName(AMOUNT)
    @Column(columnName = AMOUNT)
    private Double amount;
    
    @SerializedName(PAYMENT_STATUS)
    @Column(columnName = PAYMENT_STATUS)
    private boolean paymentStatus = false;
    
    @SerializedName(MONEY_JAR_ID)
    @Column(columnName = MONEY_JAR_ID)
    private String jarId;
    
    @SerializedName(PAYMENT_TIMESTAMP)
    @Column(columnName = PAYMENT_TIMESTAMP, characterLength = 45)
    private String paymentTimestamp;
    
    @SerializedName(CREATED_AT)
    @Column(columnName = CREATED_AT, characterLength = 45)
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }

    public String getJarId() {
        return jarId;
    }

    public void setPaymentTimestamp(String paymentTimestamp) {
        this.paymentTimestamp = paymentTimestamp;
    }

    public String getPaymentTimestamp() {
        return paymentTimestamp;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

package com.benardmathu.hfms.data.transactions.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TRANSACTION_TB_NAME)
public class Transaction {
    @SerializedName(TRANSACTION_ID)
    @Id
    private Long id;
    
    @SerializedName(TRANSACTION_DESCRIPTION)
    @Column(name = TRANSACTION_DESCRIPTION, length = 1024)
    private String transactionDesc;
    
    @SerializedName(PAYMENT_DETAILS)
    @Column(name = PAYMENT_DETAILS, length = 255, nullable = false)
    private String paymentDetails;
    
    @SerializedName(AMOUNT)
    @Column(name = AMOUNT)
    private double amount;
    
    @SerializedName(PAYMENT_STATUS)
    @Column(name = PAYMENT_STATUS)
    private boolean paymentStatus = false;
    
    @SerializedName(USER_ID)
    @Column(name = USER_ID)
    private Long userId;
    
    @SerializedName(PAYMENT_TIMESTAMP)
    @Column(name = PAYMENT_TIMESTAMP, length = 45, nullable = false)
    private String paymentTimestamp;
    
    @SerializedName(CREATED_AT)
    @Column(name = CREATED_AT, length = 45)
    private String createdAt;
}

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
@Table(name = DbEnvironment.TRANSACTION_TB_NAME)
public class Transaction {
    @SerializedName(DbEnvironment.TRANSACTION_ID)
    @Id
    private Long id;
    
    @SerializedName(DbEnvironment.TRANSACTION_DESCRIPTION)
    @Column(name = DbEnvironment.TRANSACTION_DESCRIPTION, length = 1024)
    private String transactionDesc;
    
    @SerializedName(DbEnvironment.PAYMENT_DETAILS)
    @Column(name = DbEnvironment.PAYMENT_DETAILS, length = 255, nullable = false)
    private String paymentDetails;
    
    @SerializedName(DbEnvironment.AMOUNT)
    @Column(name = DbEnvironment.AMOUNT)
    private double amount;
    
    @SerializedName(DbEnvironment.PAYMENT_STATUS)
    @Column(name = DbEnvironment.PAYMENT_STATUS)
    private boolean paymentStatus = false;
    
    @SerializedName(DbEnvironment.USER_ID)
    @Column(name = DbEnvironment.USER_ID)
    private Long userId;
    
    @SerializedName(DbEnvironment.PAYMENT_TIMESTAMP)
    @Column(name = DbEnvironment.PAYMENT_TIMESTAMP, length = 45, nullable = false)
    private String paymentTimestamp;
    
    @SerializedName(DbEnvironment.CREATED_AT)
    @Column(name = DbEnvironment.CREATED_AT, length = 45)
    private String createdAt;
}

package com.benardmathu.hfms.data.expense.model;

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
@Table(name = EXPENSES_TB_NAME)
public class Expense {
    @SerializedName(EXPENSE_ID)
    @Id
    private Long expenseId = "";

    @SerializedName(AMOUNT)
    @Column(name = AMOUNT)
    private double amount = 0;

    @SerializedName(MONEY_JAR_ID)
    @Column(name = MONEY_JAR_ID)
    private String jarId = "";

    @SerializedName(PAYEE_NAME)
    @Column(name = PAYEE_NAME, length = 45)
    private String payee = "";

    @SerializedName(BUSINESS_NUMBER)
    @Column(name = BUSINESS_NUMBER, nullable = false)
    private String businessNumber = "";

    @SerializedName(ACCOUNT_NUMBER)
    @Column(name = ACCOUNT_NUMBER, nullable = false)
    private String accountNumber = "";
}

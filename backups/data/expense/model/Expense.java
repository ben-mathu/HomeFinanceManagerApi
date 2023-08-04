package com.benardmathu.hfms.data.expense.model;

import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbEnvironment.EXPENSES_TB_NAME)
public class Expense {
    @SerializedName(DbEnvironment.EXPENSE_ID)
    @Id
    private Long expenseId;

    @SerializedName(DbEnvironment.AMOUNT)
    @Column(name = DbEnvironment.AMOUNT)
    private double amount = 0;

    @SerializedName(DbEnvironment.MONEY_JAR_ID)
    @ManyToOne
    private MoneyJar jar;

    @SerializedName(DbEnvironment.PAYEE_NAME)
    @Column(name = DbEnvironment.PAYEE_NAME, length = 45)
    private String payee = "";

    @SerializedName(DbEnvironment.BUSINESS_NUMBER)
    @Column(name = DbEnvironment.BUSINESS_NUMBER, nullable = false)
    private String businessNumber = "";

    @SerializedName(DbEnvironment.ACCOUNT_NUMBER)
    @Column(name = DbEnvironment.ACCOUNT_NUMBER, nullable = false)
    private String accountNumber = "";
}

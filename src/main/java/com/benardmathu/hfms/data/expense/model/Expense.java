package com.benardmathu.hfms.data.expense.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Column;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.PrimaryKey;
import com.benardmathu.hfms.init.Table;
import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(
        tableName = EXPENSES_TB_NAME,
        constraint = {@Constraint(
                name = FK_EXPENSES_JAR_ID,
                columnName = MONEY_JAR_ID,
                tableName = MONEY_JAR_TB_NAME
        )}
)
public class Expense {
    @SerializedName(EXPENSE_ID)
    @PrimaryKey(columnName = EXPENSE_ID)
    private String expenseId = "";

//    @SerializedName(EXPENSE_NAME)
//    @Column(columnName = EXPENSE_NAME, characterLength = 45, unique = true)
//    private String name = "";
//
//    @SerializedName(EXPENSE_DESCRIPTION)
//    @Column(columnName = EXPENSE_DESCRIPTION, characterLength = 255)
//    private String description = "";

    @SerializedName(AMOUNT)
    @Column(columnName = AMOUNT)
    private double amount = 0;

    @SerializedName(MONEY_JAR_ID)
    @Column(columnName = MONEY_JAR_ID)
    private String jarId = "";

    @SerializedName(PAYEE_NAME)
    @Column(columnName = PAYEE_NAME, characterLength = 45)
    private String payee = "";

    @SerializedName(BUSINESS_NUMBER)
    @Column(columnName = BUSINESS_NUMBER, notNull = false)
    private String businessNumber = "";

    @SerializedName(ACCOUNT_NUMBER)
    @Column(columnName = ACCOUNT_NUMBER, notNull = false)
    private String accountNumber = "";

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public String getJarId() {
        return jarId;
    }

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }
}

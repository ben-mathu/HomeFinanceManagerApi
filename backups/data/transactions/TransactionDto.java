package com.benardmathu.hfms.data.transactions;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.transactions.model.Transaction;
import static com.benardmathu.hfms.data.utils.DbEnvironment.TRANSACTION_TB_NAME;
import java.util.List;

/**
 * A data transfer object
 * @author bernard
 */
public class TransactionDto {
    @SerializedName(DbEnvironment.TRANSACTION_TB_NAME)
    private List<Transaction> transactions;
    @SerializedName("income_monthly")
    private List<Double> amounts;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Double> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Double> amounts) {
        this.amounts = amounts;
    }
}

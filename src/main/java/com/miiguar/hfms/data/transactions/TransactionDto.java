package com.miiguar.hfms.data.transactions;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.transactions.model.Transaction;
import static com.miiguar.hfms.data.utils.DbEnvironment.TRANSACTION_TB_NAME;
import java.util.List;

/**
 * A data transfer object
 * @author bernard
 */
public class TransactionDto {
    @SerializedName(TRANSACTION_TB_NAME)
    private List<Transaction> transactions;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

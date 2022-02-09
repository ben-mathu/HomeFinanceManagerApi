package com.benardmathu.hfms.data.transactions;

import com.benardmathu.hfms.data.transactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

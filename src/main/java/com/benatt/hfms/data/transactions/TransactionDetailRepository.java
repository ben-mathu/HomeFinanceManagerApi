package com.benatt.hfms.data.transactions;

import com.benatt.hfms.data.transactions.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<Transaction, Long> {
}

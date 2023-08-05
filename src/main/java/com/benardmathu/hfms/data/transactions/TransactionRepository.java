package com.benardmathu.hfms.data.transactions;

import com.benardmathu.hfms.data.transactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserId(Long userId);
}

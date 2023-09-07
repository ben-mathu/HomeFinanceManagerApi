package com.benatt.hfms.services;

import com.benatt.hfms.data.transactions.models.Transaction;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<Transaction> mapToCategory(Long transactionId, Long categoryId);
}

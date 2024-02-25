package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.data.transactions.TransactionDetailRepository;
import com.benatt.hfms.data.transactions.models.Transaction;
import com.benatt.hfms.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<Transaction> mapToCategory(Long transactionId, Long categoryId) {
        Transaction transaction = transactionDetailRepository.findById(transactionId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (transaction == null || category == null)
            throw new InvalidParameterException("Transaction or category not found");

        transaction.setCategory(category);
        return ResponseEntity.ok(transactionDetailRepository.save(transaction));
    }

    @Override
    public ResponseEntity<Transaction> deleteTransaction(Long transactionId) {
        Transaction transaction = transactionDetailRepository.findById(transactionId).orElse(null);
        if (transaction != null)
            transactionDetailRepository.delete(transaction);
        else throw new InvalidParameterException("Transaction not found");

        return ResponseEntity.ok(transaction);
    }
}

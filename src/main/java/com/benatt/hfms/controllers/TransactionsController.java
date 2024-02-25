package com.benatt.hfms.controllers;

import com.benatt.hfms.data.transactions.models.Transaction;
import com.benatt.hfms.services.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transactions")
public class TransactionsController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @PutMapping("map-to-category/{transactionId}")
    private ResponseEntity<Transaction> mapToCategory(@PathVariable("transactionId") Long transactionId,
                                                      @RequestParam("categoryId") Long categoryId) {
        return transactionService.mapToCategory(transactionId, categoryId);
    }

    @DeleteMapping("transactionId")
    private ResponseEntity<Transaction> deleteTransaction(@PathVariable("transactionId") Long transactionId) {
        return transactionService.deleteTransaction(transactionId);
    }
}

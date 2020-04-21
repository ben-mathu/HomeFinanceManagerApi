package com.benardmathu.hfms.data.transactions;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.transactions.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bernard
 */
public class TransactionService implements BaseService<Transaction> {
    public static final String TAG = TransactionService.class.getSimpleName();

    @Autowired
    private TransactionRepository repository;

    @Override
    public Transaction save(Transaction item) {
        return repository.save(item);
    }

    @Override
    public Transaction update(Transaction item) {
        return repository.save(item);
    }

    @Override
    public void delete(Transaction item) {
        repository.delete(item);
    }

    @Override
    public Transaction get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public List<Transaction> getAll(Long id) {
        return null;
    }

    @Override
    public List<Transaction> saveAll(ArrayList<Transaction> items) {
        return repository.findAll();
    }

    public List<Transaction> getAllByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }
}

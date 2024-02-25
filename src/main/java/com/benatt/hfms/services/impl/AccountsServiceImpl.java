package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.accounts.dtos.AccountPaidInRequest;
import com.benatt.hfms.data.accounts.dtos.AccountPaidOutRequest;
import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.logs.dtos.Result;
import com.benatt.hfms.data.transactions.TransactionDetailRepository;
import com.benatt.hfms.data.transactions.models.Transaction;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class AccountsServiceImpl implements AccountsService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Override
    @Transactional
    public ResponseEntity<Account> addAccount(AccountRequest request) {
        Account account = accountsRepository.findByName(request.getAccountName());
        if (account != null) {
            account.setBalance(request.getBalance());
            return ResponseEntity.ok(accountsRepository.save(account));
        } else {
            account = new Account();
            account.setId(request.getId());
            account.setBalance(request.getBalance());
            account.setName(request.getAccountName());
            return ResponseEntity.ok(accountsRepository.save(account));
        }
    }

    @Override
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountsRepository.findAll());
    }

    @Override
    public ResponseEntity<Account> updateAccount(AccountRequest request, Long id) {
        Account account = accountsRepository.findById(id).orElse(null);

        if (account == null) {
            throw new InvalidParameterException("Could not find Account with ID: " + id);
        }

        account.setName(request.getAccountName());
        account.setBalance(request.getBalance());
        return ResponseEntity.ok(accountsRepository.save(account));
    }

    @Override
    public ResponseEntity<Result> deleteAccount(Long id) throws InvalidFieldException {
        Account account = accountsRepository.findById(id).orElse(null);

        if (account == null)
            throw new InvalidFieldException("Could not find Account with ID: " + id);

        accountsRepository.delete(account);
        return ResponseEntity.ok(new Result("Account: " + account.getName() + " deleted successfully"));
    }

    @Override
    public ResponseEntity<Transaction> addPaidOutAmount(Long accountId, AccountPaidOutRequest request) throws InvalidFieldException {
        Account account = accountsRepository.findById(accountId).orElse(null);
        if (account == null)
            throw new InvalidFieldException("Could not find Account with ID: " + accountId);

        double balance = account.getBalance() - request.getAmount();
//        if (balance < 0)
//            throw new InvalidFieldException("Account balance is less than 0");

        account.setBalance(balance);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setPaidTo(request.getPaidTo());
        transaction.setPaidOut(request.getAmount());
        transaction.setDescription(request.getTransactionDetails());
        return ResponseEntity.ok(transactionDetailRepository.save(transaction));
    }

    @Override
    public ResponseEntity<Transaction> addPaidInAmount(Long accountId, AccountPaidInRequest request) throws InvalidFieldException {
        Account account = accountsRepository.findById(accountId).orElse(null);
        if (account == null)
            throw new InvalidFieldException("Could not find Account with ID: " + accountId);

        double balance = account.getBalance() + request.getAmount();
        account.setBalance(balance);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setPaidFrom(request.getPaidFrom());
        transaction.setPaidIn(request.getAmount());
        transaction.setDescription(request.getTransactionDetails());
        return ResponseEntity.ok(transactionDetailRepository.save(transaction));
    }
}

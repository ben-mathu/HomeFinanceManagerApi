package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.accounts.TransactionDetailRepository;
import com.benatt.hfms.data.accounts.dtos.AccountPaidInRequest;
import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.dtos.AccountPaidOutRequest;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.accounts.models.TransactionDetail;
import com.benatt.hfms.data.logs.dtos.Result;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class AccountsServiceImpl implements AccountsService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Override
    public ResponseEntity<Account> addAccount(AccountRequest request) {
        Account account = new Account();
        account.setBalance(request.getBalance());
        account.setName(request.getAccountName());
        return ResponseEntity.ok(accountsRepository.save(account));
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
        return ResponseEntity.ok(new Result("Account: " + id + " deleted successfully"));
    }

    @Override
    public ResponseEntity<TransactionDetail> addPaidOutAmount(Long accountId, AccountPaidOutRequest request) throws InvalidFieldException {
        Account account = accountsRepository.findById(accountId).orElse(null);
        if (account == null)
            throw new InvalidFieldException("Could not find Account with ID: " + accountId);

        double balance = account.getBalance() - request.getAmount();
        if (balance < 0)
            throw new InvalidFieldException("Account balance is less than 0");

        account.setBalance(balance);

        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setAccount(account);
        transactionDetail.setPaidTo(request.getPaidTo());
        transactionDetail.setPaidIn(request.getAmount());
        return ResponseEntity.ok(transactionDetailRepository.save(transactionDetail));
    }

    @Override
    public ResponseEntity<TransactionDetail> addPaidInAmount(Long accountId, AccountPaidInRequest request) throws InvalidFieldException {
        Account account = accountsRepository.findById(accountId).orElse(null);
        if (account == null)
            throw new InvalidFieldException("Could not find Account with ID: " + accountId);

        double balance = account.getBalance() + request.getAmount();
        account.setBalance(balance);

        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setAccount(account);
        transactionDetail.setPaidTo(request.getPaidFrom());
        transactionDetail.setPaidIn(request.getAmount());
        return ResponseEntity.ok(transactionDetailRepository.save(transactionDetail));
    }
}

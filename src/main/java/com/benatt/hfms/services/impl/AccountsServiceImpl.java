package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.models.Account;
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

        if (account == null) {
            throw new InvalidFieldException("Could not find Account with ID: " + id);
        }
        accountsRepository.delete(account);
        return ResponseEntity.ok(new Result("Account: " + id + " deleted successfully"));
    }
}

package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.services.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountsServiceImpl implements AccountsService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public Account addAccount(AccountRequest request) {
        Account account = new Account();
        account.setBalance(request.getBalance());
        account.setName(request.getAccountName());
        return accountsRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountsRepository.findAll();
    }
}

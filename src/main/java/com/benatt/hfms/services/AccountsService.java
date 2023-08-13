package com.benatt.hfms.services;

import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.logs.dtos.Result;
import com.benatt.hfms.exceptions.InvalidFieldException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountsService {
    ResponseEntity<Account> addAccount(AccountRequest request);

    ResponseEntity<List<Account>> getAllAccounts();

    ResponseEntity<Account> updateAccount(AccountRequest request, Long id);

    ResponseEntity<Result> deleteAccount(Long id) throws InvalidFieldException;
}

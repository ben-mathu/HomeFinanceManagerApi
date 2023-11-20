package com.benatt.hfms.controllers;

import com.benatt.hfms.data.accounts.dtos.AccountPaidInRequest;
import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.dtos.AccountPaidOutRequest;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.transactions.models.Transaction;
import com.benatt.hfms.data.logs.dtos.Result;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.impl.AccountsServiceImpl;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "accounts", name = "Accounts")
public class AccountsController {
    @Autowired
    private AccountsServiceImpl accountsService;

    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody AccountRequest request) throws PSQLException {
        return accountsService.addAccount(request);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return accountsService.getAllAccounts();
    }

    @PutMapping("{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Long id, @RequestBody AccountRequest request) {
        return accountsService.updateAccount(request, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteAccount(@PathVariable("id") Long id) throws InvalidFieldException {
        return accountsService.deleteAccount(id);
    }

    @PostMapping("{accountId}/paidOut")
    public ResponseEntity<Transaction> paidOut(@PathVariable("accountId") Long accountId,
                                               @RequestBody AccountPaidOutRequest request) throws InvalidFieldException {

        return accountsService.addPaidOutAmount(accountId, request);
    }

    @PostMapping("{accountId}/paidIn")
    public ResponseEntity<Transaction> paidIn(@PathVariable("accountId") Long accountId,
                                              @RequestBody AccountPaidInRequest request) throws InvalidFieldException {

        return accountsService.addPaidInAmount(accountId, request);
    }
}

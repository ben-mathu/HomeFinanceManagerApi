package com.benatt.hfms.controllers;

import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.services.impl.AccountsServiceImpl;
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
    public ResponseEntity<Account> addAccount(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody AccountRequest request) {
        return ResponseEntity.ok(accountsService.addAccount(request));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountsService.getAllAccounts());
    }
}

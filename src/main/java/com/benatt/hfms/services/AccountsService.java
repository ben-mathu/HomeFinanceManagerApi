package com.benatt.hfms.services;

import com.benatt.hfms.data.accounts.dtos.AccountRequest;
import com.benatt.hfms.data.accounts.models.Account;

import java.util.List;

public interface AccountsService {
    Account addAccount(AccountRequest request);

    List<Account> getAllAccounts();
}

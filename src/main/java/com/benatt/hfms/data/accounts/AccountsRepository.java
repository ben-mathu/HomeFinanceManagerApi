package com.benatt.hfms.data.accounts;

import com.benatt.hfms.data.accounts.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);
}

package com.benardmathu.hfms.data.status;

import com.benardmathu.hfms.data.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author bernard
 */
@Service
public class AccountStatusService implements BaseService<AccountStatus> {
    public static final String TAG = AccountStatusService.class.getSimpleName();

    @Autowired
    private AccountStatusRepository repository;

    @Override
    public AccountStatus save(AccountStatus item) {
        return repository.save(item);
    }

    @Override
    public AccountStatus update(AccountStatus item) {
        return repository.save(item);
    }

    @Override
    public void delete(AccountStatus item) {
        repository.delete(item);
    }

    @Override
    public AccountStatus get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AccountStatus> getAll() {
        return null;
    }

    @Override
    public List<AccountStatus> saveAll(ArrayList<AccountStatus> items) {
        return repository.saveAll(items);
    }

    public boolean updateJarStatus(AccountStatus accountStatus) {
        accountStatus = repository.save(accountStatus);
        return accountStatus != null;
    }

    public boolean updateIncomeStatus(AccountStatus accountStatus) {
        Optional<AccountStatus> optional = repository.findByUserId(accountStatus.getUserId());
        AccountStatus status = optional.orElse(null);
        if (status == null)
            return true;
        status.setIncomeStatus(accountStatus.getIncomeStatus());
        accountStatus = repository.save(accountStatus);
        return false;
    }

    public boolean updateHouseholdStatus(AccountStatus accountStatus) {
        accountStatus = repository.save(accountStatus);
        return accountStatus != null;
    }
}

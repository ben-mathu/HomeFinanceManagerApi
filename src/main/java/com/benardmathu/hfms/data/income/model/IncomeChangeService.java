package com.benardmathu.hfms.data.income.model;

import static com.benardmathu.hfms.data.utils.DbEnvironment.AMOUNT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.CREATED_AT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.INCOME_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.ON_CHANGE_INCOME_STATUS;
import static com.benardmathu.hfms.data.utils.DbEnvironment.ON_UPDATE_INCOME;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.income.IncomeChangeRepository;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author bernard
 */
@Service
public class IncomeChangeService implements BaseService<OnInComeChange> {

    @Autowired
    private IncomeChangeRepository repository;

    @Override
    public OnInComeChange get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<OnInComeChange> getAll() {
        return repository.findAll();
    }

    @Override
    public List<OnInComeChange> saveAll(ArrayList<OnInComeChange> items) {
        return repository.saveAll(items);
    }

    public List<OnInComeChange> getAll(Long id, Date from, Date to) {
        return repository.findByIdAndCreatedAtGreaterThanEqual(id, from, to);
    }

    @Override
    public OnInComeChange save(OnInComeChange item) {
        return repository.save(item);
    }

    @Override
    public OnInComeChange update(OnInComeChange item) {
        return repository.save(item);
    }

    @Override
    public void delete(OnInComeChange item) {
        repository.delete(item);
    }
}

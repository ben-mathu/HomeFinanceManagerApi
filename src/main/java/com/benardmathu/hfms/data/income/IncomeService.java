package com.benardmathu.hfms.data.income;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * Member methods to create and update the income table
 * @author bernard
 */
public class IncomeService implements BaseService<Income> {
    public static final String TAG = IncomeService.class.getSimpleName();

    @Autowired
    private IncomeRepository incomeRepository;

    @Override
    public Income save(Income item) {
        return incomeRepository.save(item);
    }

    @Override
    public Income update(Income item) {
        return incomeRepository.save(item);
    }

    @Override
    public void delete(Income item) {
        incomeRepository.delete(item);
    }

    @Override
    public Income get(Long id) {
        Optional<Income> optional =  incomeRepository.findByUserId(id);
        return optional.orElse(null);
    }

    @Override
    public List<Income> getAll() {
        return incomeRepository.findAll();
    }

    @Override
    public List<Income> saveAll(ArrayList<Income> items) {
        return incomeRepository.saveAll(items);
    }
}

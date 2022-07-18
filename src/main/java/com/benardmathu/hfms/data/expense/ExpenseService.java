package com.benardmathu.hfms.data.expense;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.expense.model.Expense;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class ExpenseService implements BaseService<Expense> {
    public static final String TAG = ExpenseService.class.getSimpleName();

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Expense update(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public void delete(Expense item) {
        expenseRepository.delete(item);
    }

    @Override
    public Expense get(Long id) {
        Optional<Expense> optional = expenseRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    public List<Expense> getAllByJarId(Long id) {
        return expenseRepository.findAllByMoneyJarId(id);
    }

    @Override
    public List<Expense> saveAll(ArrayList<Expense> items) {
        return expenseRepository.saveAll(items);
    }
}

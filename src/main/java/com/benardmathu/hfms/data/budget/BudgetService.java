package com.benardmathu.hfms.data.budget;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.budget.model.Budget;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * @author bernard
 */
@Component
public class BudgetService implements BaseService<Budget> {
    public static final String TAG = BudgetService.class.getSimpleName();

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget getBudgetByHouseholdId(Long id) {
        Optional<Budget> budgetOptional = budgetRepository.getBudgetByHouseholdId(id);
        return budgetOptional.orElse(null);
    }

    @Override
    public Budget save(Budget item) {
        return budgetRepository.save(item);
    }

    @Override
    public int update(Budget item) {
        return 0;
    }

    @Override
    public int delete(Budget item) {
        return 0;
    }

    @Override
    public Budget get(String id) {
        return null;
    }

    @Override
    public List<Budget> getAll() {
        return null;
    }

    @Override
    public List<Budget> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<Budget> items) {
        return 0;
    }
}

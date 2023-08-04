package com.benardmathu.hfms.data.budget;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.budget.model.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Budget update(Budget item) {
        return budgetRepository.save(item);
    }

    @Override
    public void delete(Budget item) {
        budgetRepository.delete(item);
    }

    @Override
    public Budget get(Long id) {
        return null;
    }

    @Override
    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }

    @Override
    public List<Budget> saveAll(ArrayList<Budget> items) {
        return budgetRepository.saveAll(items);
    }
}

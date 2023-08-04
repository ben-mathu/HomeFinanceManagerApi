package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Budget saveBudget(BudgetRequest request) throws InvalidFieldException {
        if (request.getCategory().isEmpty()) {
            throw new InvalidFieldException("Error invalid value in field: Category");
        }

        Budget budget = new Budget();
        budget.setCategoryType(request.getCategory());

        budget = budgetRepository.save(budget);
        return budget;
    }
}

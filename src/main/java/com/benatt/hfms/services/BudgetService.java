package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.dtos.MonthlySummaryResponse;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;

public interface BudgetService {
    Budget saveBudget(BudgetRequest request) throws InvalidFieldException;

    Budget addCategory(CategoryRequest request, Long id);

    MonthlySummaryResponse calculateMonthlySummary() throws EmptyResultException;
}

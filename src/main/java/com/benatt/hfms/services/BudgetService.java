package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.dtos.MonthlySummaryResponse;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;

import java.util.List;

public interface BudgetService {
    Budget saveBudget(BudgetRequest request, Long accountId) throws InvalidFieldException;

    MonthlySummaryResponse calculateMonthlySummary() throws EmptyResultException;

    List<Budget> getAll();
}

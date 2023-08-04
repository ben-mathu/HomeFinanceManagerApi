package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.exceptions.InvalidFieldException;

public interface BudgetService {
    Budget saveBudget(BudgetRequest request) throws InvalidFieldException;
}

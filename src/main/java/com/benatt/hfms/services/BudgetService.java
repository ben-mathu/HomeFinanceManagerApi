package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.exceptions.BadRequestException;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BudgetService {
    ResponseEntity<Budget> saveBudget(BudgetRequest request) throws InvalidFieldException, BadRequestException;

    ResponseEntity<Budget> getBudget() throws EmptyResultException;

    ResponseEntity<Budget> updateBudget(Long budgetId, BudgetRequest request);
}

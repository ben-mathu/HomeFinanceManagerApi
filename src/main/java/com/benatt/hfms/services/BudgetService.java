package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.exceptions.InvalidFieldException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BudgetService {
    ResponseEntity<Budget> saveBudget(BudgetRequest request) throws InvalidFieldException;

    List<Budget> getAll();
}

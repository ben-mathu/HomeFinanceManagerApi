package com.benatt.hfms.controllers;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.dtos.MonthlySummaryResponse;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.impl.BudgetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("budget")
public class BudgetController {

    @Autowired
    private BudgetServiceImpl budgetService;

    @PostMapping
    public ResponseEntity<Budget> addBudget(@RequestBody BudgetRequest request) throws InvalidFieldException {
        return ResponseEntity.ok(budgetService.saveBudget(request));
    }

    @GetMapping("{budgetId}/categories/")
    public ResponseEntity<Budget> addCategory(@RequestBody CategoryRequest request, @PathVariable("budgetId") Long id) {
        return ResponseEntity.ok(budgetService.addCategory(request, id));
    }

    @GetMapping("calculate-monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> calculateMonthlySummary() throws EmptyResultException {
        return ResponseEntity.ok(budgetService.calculateMonthlySummary());
    }
}

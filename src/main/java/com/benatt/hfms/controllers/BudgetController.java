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

import java.util.List;

@RestController
@RequestMapping("budget")
public class BudgetController {

    @Autowired
    private BudgetServiceImpl budgetService;

    @PostMapping
    public ResponseEntity<Budget> addBudget(@RequestParam("accountId") Long accountId,
                                            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody BudgetRequest request)
            throws InvalidFieldException {

        return ResponseEntity.ok(budgetService.saveBudget(request, accountId));
    }

    @GetMapping("calculate-monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> calculateMonthlySummary() throws EmptyResultException {
        return ResponseEntity.ok(budgetService.calculateMonthlySummary());
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getAll() {
        return ResponseEntity.ok(budgetService.getAll());
    }
}

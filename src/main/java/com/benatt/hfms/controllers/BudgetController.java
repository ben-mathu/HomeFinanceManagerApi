package com.benatt.hfms.controllers;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.exceptions.BadRequestException;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.impl.BudgetServiceImpl;
import com.benatt.hfms.services.impl.CategoriesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("budget")
public class BudgetController {

    @Autowired
    private BudgetServiceImpl budgetService;

    @Autowired
    private CategoriesServiceImpl categoriesService;

    @PostMapping
    public ResponseEntity<Budget> addBudget(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody BudgetRequest request)
            throws InvalidFieldException, BadRequestException {

        return budgetService.saveBudget(request);
    }

    @PostMapping("by-rule")
    public ResponseEntity<List<Category>> saveBudgetByCategoryRule(@RequestBody BudgetRequest request)
            throws InvalidFieldException, BadRequestException {

        return categoriesService.saveBudgetByCategoryRule(request);
    }

    @GetMapping
    public ResponseEntity<Budget> getBudget() throws EmptyResultException {
        return budgetService.getBudget();
    }

    @PutMapping("{budgetId}")
    public ResponseEntity<Budget> updateBudget(@PathVariable("budgetId") Long budgetId, @RequestBody BudgetRequest request) {
        return budgetService.updateBudget(budgetId, request);
    }
}

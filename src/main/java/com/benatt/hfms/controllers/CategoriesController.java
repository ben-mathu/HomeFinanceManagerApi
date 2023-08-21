package com.benatt.hfms.controllers;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.impl.CategoriesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    @Autowired
    private CategoriesServiceImpl categoriesService;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody CategoryRequest request,
                                                @RequestParam("budgetId") Long id) {
        return categoriesService.addCategory(request, id);
    }

    @PostMapping("by-rule")
    public ResponseEntity<Budget> saveBudgetByCategoryRule(BudgetRequest request) throws InvalidFieldException {
        return categoriesService.saveBudgetByCategoryRule(request);
    }
}

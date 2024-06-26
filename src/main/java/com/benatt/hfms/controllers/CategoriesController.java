package com.benatt.hfms.controllers;

import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.data.logs.dtos.Result;
import com.benatt.hfms.services.impl.CategoriesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    @Autowired
    private CategoriesServiceImpl categoriesService;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody CategoryRequest request, @RequestParam("budgetId") Long id) {
        return categoriesService.addCategory(request, id);
    }

    @PutMapping
    public ResponseEntity<Category> updateCategory(@RequestBody CategoryRequest request, @RequestParam("categoryId") Long categoryId) {
        return categoriesService.updateCategory(request, categoryId);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategoryByBudgetId(@RequestParam("budgetId") Long budgetId) {
        return categoriesService.getAllByBudgetId(budgetId);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<Result> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        return categoriesService.deleteCategory(categoryId);
    }
}

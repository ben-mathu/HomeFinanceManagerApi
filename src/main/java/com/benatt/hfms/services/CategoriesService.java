package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import org.springframework.http.ResponseEntity;

public interface CategoriesService {

    ResponseEntity<Category> addCategory(CategoryRequest request, Long id);

    ResponseEntity<Budget> saveBudgetByCategoryRule(BudgetRequest request);
}

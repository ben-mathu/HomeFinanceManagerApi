package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoriesService {

    ResponseEntity<Category> addCategory(CategoryRequest request, Long id);

    ResponseEntity<List<Category>> saveBudgetByCategoryRule(BudgetRequest request) throws BadRequestException;

    ResponseEntity<List<Category>> getAllByBudgetId(Long budgetId);
}

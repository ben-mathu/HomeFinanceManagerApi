package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public ResponseEntity<Category> addCategory(CategoryRequest request, Long id) {
        Budget budget = budgetRepository.findById(id).orElse(null);

        if (budget == null)
            throw new InvalidParameterException("Budget with id: " + id + " was not found.");

        double totalBudgetAmount = budget.getAmountBudgeted();
        double categoryAmount = request.getAmount();
        if (categoryAmount > totalBudgetAmount)
            throw new InvalidParameterException(request.getCategoryName() + " has amount greater than total budgeted amount: " + totalBudgetAmount);

        double percentageOfAmount = categoryAmount / totalBudgetAmount * 100;

        Category category = new Category();
        category.setPercentage(percentageOfAmount);
        category.setCategoryType(request.getCategoryType());
        category.setName(request.getCategoryName());

        List<Category> categories;
        if (budget.getCategories().isEmpty()) {
            categories = new ArrayList<>();
            categories.add(category);
        } else {
            categories = budget.getCategories();
            categories.add(category);
        }
        budget.setCategories(categories);
        budgetRepository.save(budget);
        return ResponseEntity.ok(category);
    }
}

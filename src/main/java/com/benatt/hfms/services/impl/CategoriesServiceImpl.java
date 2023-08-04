package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Category addCategory(CategoryRequest request, Long id) {
        Budget budget = budgetRepository.findById(id).orElse(null);

        if (budget == null)
            throw new InvalidParameterException("Budget with id: " + id + " was not found.");

        Category category = new Category();
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
        return category;
    }

    @Override
    public Category addPaidOutAmount(Long categoryId, double amount) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category == null)
            throw new InvalidParameterException("Category with id " + categoryId + " was not found.");

        category.setPaidOut(category.getPaidOut() + amount);

        return categoryRepository.save(category);
    }

    @Override
    public Category addPaidInAmount(Long categoryId, double amount) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category == null)
            throw new InvalidParameterException("Category with id " + categoryId + " was not found.");

        category.setPaidIn(category.getPaidIn() + amount);

        return categoryRepository.save(category);
    }
}

package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.data.category.models.CategoryType;
import com.benatt.hfms.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.benatt.hfms.utils.Contants.RULE;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public ResponseEntity<Category> addCategory(CategoryRequest request, Long id) {
        Budget budget = budgetRepository.findById(id).orElse(null);

        Category category = getCategory(request, id, budget);

        category.setBudget(budget);
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    private static Category getCategory(CategoryRequest request, Long id, Budget budget) {
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
        return category;
    }

    @Override
    public ResponseEntity<Budget> saveBudgetByCategoryRule(BudgetRequest request) {
        Budget budget = new Budget();
        budget.setBudgetPeriod(request.getPeriod());
        budget.setAmountBudgeted(request.getBudgetAmount());

        int[] ruleList = Arrays.stream(RULE.split("-")).mapToInt(Integer::parseInt).toArray();
        List<Category> categories = new ArrayList<>();

        Category category = new Category();
        category.setCategoryType(CategoryType.NEEDS);
        category.setPercentage(ruleList[0]);
        categories.add(category);

        category = new Category();
        category.setCategoryType(CategoryType.WANTS);
        category.setPercentage(ruleList[1]);
        categories.add(category);

        category = new Category();
        category.setCategoryType(CategoryType.SAVINGS);
        category.setPercentage(ruleList[2]);
        categories.add(category);

        budget.setCategories(categories);
        return ResponseEntity.ok(budgetRepository.save(budget));
    }
}

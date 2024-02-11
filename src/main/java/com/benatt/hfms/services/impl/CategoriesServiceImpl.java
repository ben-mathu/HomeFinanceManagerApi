package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.data.category.models.CategoryType;
import com.benatt.hfms.data.logs.dtos.Result;
import com.benatt.hfms.exceptions.BadRequestException;
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
            throw new InvalidParameterException(request.getCategoryType() + " amount must not be more than budget amount " + totalBudgetAmount);

        double percentageOfAmount = categoryAmount / totalBudgetAmount * 100;

        Category category = new Category();
        category.setPercentage(percentageOfAmount);
        category.setCategoryType(request.getCategoryType());
        return category;
    }

    @Override
    public ResponseEntity<Budget> saveBudgetByCategoryRule(BudgetRequest request) throws BadRequestException {
        Budget budget = new Budget();
        budget.setBudgetPeriod(request.getPeriod());
        budget.setAmountBudgeted(request.getBudgetAmount());
        budget = budgetRepository.save(budget);

        int[] ruleList = Arrays.stream(RULE.split("/")).mapToInt(Integer::parseInt).toArray();
        List<Category> categories = new ArrayList<>();

        Category category = new Category();
        category.setCategoryType(CategoryType.NEEDS);
        category.setPercentage(ruleList[0]);
        category.setBudget(budget);
        categoryRepository.save(category);
        categories.add(category);

        category = new Category();
        category.setCategoryType(CategoryType.WANTS);
        category.setPercentage(ruleList[1]);
        category.setBudget(budget);
        categoryRepository.save(category);
        categories.add(category);

        category = new Category();
        category.setCategoryType(CategoryType.SAVINGS);
        category.setPercentage(ruleList[2]);
        category.setBudget(budget);
        categoryRepository.save(category);
        categories.add(category);

        budget.setCategories(categories);
        return ResponseEntity.ok(budget);
    }

    @Override
    public ResponseEntity<List<Category>> getAllByBudgetId(Long budgetId) {
        List<Category> categoryList = categoryRepository.findByBudgetId(budgetId);
        return ResponseEntity.ok(categoryList == null ? new ArrayList<>() : categoryList);
    }

    public ResponseEntity<Result> deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        categoryRepository.delete(category);
        return ResponseEntity.ok(new Result("Category successfully deleted"));
    }

    public ResponseEntity<Category> updateCategory(CategoryRequest request, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        
        if (category == null)
            throw new InvalidParameterException(String.format("Catery id %d does not exist", categoryId));

        category.setCategoryType(request.getCategoryType());
        return ResponseEntity.ok(categoryRepository.save(category));
    }
}

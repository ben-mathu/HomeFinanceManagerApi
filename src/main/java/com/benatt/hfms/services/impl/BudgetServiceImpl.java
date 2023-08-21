package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.wishlist.WishListRepository;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.BudgetService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private Logger logger;

    @Override
    public ResponseEntity<Budget> saveBudget(BudgetRequest request) throws InvalidFieldException {
        Budget budget = new Budget();
        budget.setAmountBudgeted(request.getBudgetAmount());
        budget.setBudgetPeriod(request.getPeriod());

        return ResponseEntity.ok(budgetRepository.save(budget));
    }

    @Override
    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }
}

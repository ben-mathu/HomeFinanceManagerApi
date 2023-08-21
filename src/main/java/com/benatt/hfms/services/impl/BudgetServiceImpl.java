package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.data.wishlist.WishListRepository;
import com.benatt.hfms.exceptions.BadRequestException;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.BudgetService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Budget> saveBudget(BudgetRequest request) throws InvalidFieldException, BadRequestException {
        List<Budget> budgetList = budgetRepository.findAll();
        if (!budgetList.isEmpty())
            throw new BadRequestException("Edit already existing budget.");

        List<Account> accountList = accountsRepository.findAll();
        double totalAmount = 0;
        for (Account account : accountList) {
            totalAmount += account.getBalance();
        }

        if (totalAmount > request.getBudgetAmount())
            throw new InvalidFieldException("Total amount in your accounts is more than what you have budgeted.");

        Budget budget = new Budget();
        budget.setAmountBudgeted(request.getBudgetAmount());
        budget.setBudgetPeriod(request.getPeriod());

        return ResponseEntity.ok(budgetRepository.save(budget));
    }

    @Override
    public ResponseEntity<Budget> getBudget() throws EmptyResultException {
        List<Budget> budgetList = budgetRepository.findAll();
        if (budgetList.isEmpty())
            throw new EmptyResultException("Empty result when requesting budget");
        return ResponseEntity.ok(budgetList.get(0));
    }

    @Override
    public ResponseEntity<Budget> updateBudget(Long budgetId, BudgetRequest request) {
        Budget budget = budgetRepository.findById(budgetId).orElse(null);
        if (budget == null)
            throw new InvalidParameterException("Could not find budget with ID: " + budgetId);

        if (request.getBudgetAmount() != null)
            budget.setAmountBudgeted(request.getBudgetAmount());

        if (request.getPeriod() != null)
            budget.setBudgetPeriod(request.getPeriod());
        return ResponseEntity.ok(budgetRepository.save(budget));
    }
}

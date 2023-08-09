package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.dtos.MonthlySummaryResponse;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.data.wishlist.WishListRepository;
import com.benatt.hfms.data.wishlist.models.WishList;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.BudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Budget saveBudget(BudgetRequest request, Long accountId) throws InvalidFieldException {
        if (request.getCategory().isEmpty()) {
            throw new InvalidFieldException("Error invalid value in field: Category");
        }

        Account account = accountsRepository.findById(accountId).orElse(null);

        if (account == null) {
            throw new InvalidFieldException("Could not find account with Id: " + accountId);
        }

        Budget budget = new Budget();
        budget.setCategoryType(request.getCategory());
        budget.setAccount(account);

        return budgetRepository.save(budget);
    }

    @Override
    public MonthlySummaryResponse calculateMonthlySummary() throws EmptyResultException {
        List<Budget> budgetList = budgetRepository.findAll();

        if (budgetList.isEmpty())
            throw new EmptyResultException("Budget list is empty");

        double totalPaidInAmount = 0;
        double totalPaidOutAmount = 0;
        double totalGrossAmount = 0;
        for (Budget budget : budgetList)
            if (!budget.getCategories().isEmpty())
                for (Category category : budget.getCategories()) {
                    totalPaidOutAmount += category.getPaidOut();
                    totalPaidInAmount += category.getPaidIn();
                    totalGrossAmount += totalPaidInAmount + totalPaidOutAmount;
                }

        MonthlySummaryResponse monthlySummaryResponse = new MonthlySummaryResponse();

        List<WishList> list = wishListRepository.findAll();
        if (list.isEmpty())
            list = new ArrayList<>();
        else
            for (WishList wishList : list) {
                if (wishList.isBought())
                    totalPaidOutAmount += wishList.getAmount();
            }

        monthlySummaryResponse.setWishList(list);
        monthlySummaryResponse.setBudgetList(budgetList);
        monthlySummaryResponse.setTotalPaidInAmount(totalPaidInAmount);
        monthlySummaryResponse.setTotalPaidOutAmount(totalPaidOutAmount);
        monthlySummaryResponse.setTotalGrossAmount(totalGrossAmount);
        return monthlySummaryResponse;
    }

    @Override
    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }
}

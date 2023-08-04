package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.dtos.MonthlySummaryResponse;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.data.wishlist.WishListRepository;
import com.benatt.hfms.data.wishlist.models.WishList;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
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

    @Override
    public Budget saveBudget(BudgetRequest request) throws InvalidFieldException {
        if (request.getCategory().isEmpty()) {
            throw new InvalidFieldException("Error invalid value in field: Category");
        }

        Budget budget = new Budget();
        budget.setCategoryType(request.getCategory());

        budget = budgetRepository.save(budget);
        return budget;
    }

    @Override
    public Budget addCategory(CategoryRequest request, Long id) {
        Budget budget = budgetRepository.findById(id).orElse(null);

        if (budget == null)
            throw new InvalidParameterException("Budget with id: " + id + " was not found.");

        Category category = new Category();
        category.setName(request.getCategoryName());

        List<Category> categoryList = null;
        if (budget.getCategories().isEmpty()) {
            categoryList = new ArrayList<>();

            categoryList.add(category);
        } else {
            categoryList = budget.getCategories();
            categoryList.add(category);
        }

        budget.setCategories(categoryList);
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
}

package com.benatt.hfms.services;

import com.benatt.hfms.data.budget.BudgetRepository;
import com.benatt.hfms.data.budget.dtos.BudgetRequest;
import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.exceptions.InvalidFieldException;
import com.benatt.hfms.services.impl.BudgetServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BudgetServiceImplTest {

    @Mock
    BudgetRepository budgetRepository;

    @InjectMocks
    BudgetServiceImpl budgetService;

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = InvalidFieldException.class)
    public void saveBudget_EmptyCategoryName_ThrowException() throws InvalidFieldException {
        budgetService.saveBudget(new BudgetRequest(""));
    }

    @Test
    public void saveBudget_ValidFields_ReturnBudget() throws InvalidFieldException {
        BudgetRequest request = new BudgetRequest("Food");
        Budget budget = new Budget();
        budget.setCategoryType(request.getCategory());
        budget.setId(0L);
        budget.setCategories(Collections.emptyList());
        when(budgetRepository.save(budget)).thenReturn(budget);

        budgetService.saveBudget(request);
    }
}
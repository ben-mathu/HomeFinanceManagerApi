package com.benatt.hfms.services;

import com.benatt.hfms.data.accounts.AccountsRepository;
import com.benatt.hfms.data.accounts.models.Account;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private AccountsRepository accountsRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = InvalidFieldException.class)
    public void saveBudget_EmptyCategoryName_ThrowException() throws InvalidFieldException {
        budgetService.saveBudget(new BudgetRequest(""), 1L);
    }

    @Test
    public void saveBudget_ValidFields_ReturnBudget() throws InvalidFieldException {
        BudgetRequest request = new BudgetRequest("Food");
        Budget budget = new Budget();
        budget.setCategoryType(request.getCategory());

        Account account = new Account(1L, "Equity", 50.0, Collections.emptyList(), Collections.emptySet());;
        when(accountsRepository.findById(1L))
                .thenReturn(Optional.of(account));

        budget.setAccount(account);

        budgetService.saveBudget(request, 1L);
        verify(budgetRepository, times(1)).save(budget);
    }
}
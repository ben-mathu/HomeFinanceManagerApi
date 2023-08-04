package com.benatt.hfms;

import com.benatt.hfms.controllers.BudgetController;
import com.benatt.hfms.services.impl.BudgetServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(BudgetController.class)
public class BudgetControllerTest {
    @Mock
    BudgetServiceImpl budgetService;

    @Test
    public void saveBudget_ValidFields_ReturnBudgetObject() {

    }
}
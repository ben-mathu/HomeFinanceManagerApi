package com.miiguar.hfms.data.jar;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.expense.ExpenseDto;
import com.miiguar.hfms.data.jar.model.MoneyJar;
import com.miiguar.hfms.data.expense.model.Expense;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.data.user.model.User;

import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.GROCERIES_TB_NAME;
import static com.miiguar.hfms.utils.Constants.*;

/**
 * @author bernard
 */
public class MoneyJarDto {
    @SerializedName(JAR)
    private MoneyJar jar;
    @SerializedName(GROCERIES_TB_NAME)
    private List<Grocery> groceries;
    @SerializedName(EXPENSE)
    private ExpenseDto expenseDto;
    @SerializedName(USER)
    private User user;

    public void setJar(MoneyJar jar) {
        this.jar = jar;
    }

    public MoneyJar getJar() {
        return jar;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<Grocery> getGroceries() {
        return groceries;
    }

    public void setGroceries(List<Grocery> groceries) {
        this.groceries = groceries;
    }

    public ExpenseDto getExpenseDto() {
        return expenseDto;
    }

    public void setExpenseDto(ExpenseDto expenseDto) {
        this.expenseDto = expenseDto;
    }
}

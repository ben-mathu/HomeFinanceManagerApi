package com.miiguar.hfms.data.container;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.container.model.Container;
import com.miiguar.hfms.data.expense.model.Expense;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.data.user.model.User;

import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.GROCERIES_TB_NAME;
import static com.miiguar.hfms.utils.Constants.*;

/**
 * @author bernard
 */
public class ContainerDto {
    @SerializedName(ENVELOPE)
    private Container envelope;
    @SerializedName(GROCERIES_TB_NAME)
    private List<Grocery> groceries;
    @SerializedName(EXPENSE)
    private Expense expense;
    @SerializedName(USER)
    private User user;

    public void setEnvelope(Container envelope) {
        this.envelope = envelope;
    }

    public Container getEnvelope() {
        return envelope;
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

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }
}

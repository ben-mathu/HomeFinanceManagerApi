package com.benardmathu.hfms.data.jar;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.expense.model.Expense;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.grocery.model.Grocery;
import com.benardmathu.hfms.data.user.model.User;

import java.util.List;

import static com.benardmathu.hfms.data.utils.DbEnvironment.GROCERIES_TB_NAME;
import static com.benardmathu.hfms.utils.Constants.*;

/**
 * @author bernard
 */
public class MoneyJarDto {
    @SerializedName(JAR_SCHEDULE_ID)
    private String id = "";
    @SerializedName(JAR)
    private MoneyJar jar;
    @SerializedName(GROCERIES_TB_NAME)
    private List<Grocery> groceries;
    @SerializedName(EXPENSE)
    private Expense expense;
    @SerializedName(USER)
    private User user;
    @SerializedName(PAYBILL)
    private String paybill;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

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

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public void setPaybill(String paybill) {
        this.paybill = paybill;
    }

    public String getPaybill() {
        return paybill;
    }
}

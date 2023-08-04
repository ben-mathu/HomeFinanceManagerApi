package com.benatt.hfms.data.budget.dtos;

import com.benatt.hfms.data.budget.models.Budget;
import com.benatt.hfms.data.wishlist.models.WishList;
import lombok.Data;

import java.util.List;

@Data
public class MonthlySummaryResponse {
    private List<Budget> budgetList;
    private List<WishList> wishList;
    private double totalPaidInAmount;
    private double totalPaidOutAmount;
    private double totalGrossAmount;
}

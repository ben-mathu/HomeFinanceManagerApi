package com.benatt.hfms.data.budget.dtos;

import com.benatt.hfms.data.budget.models.Budget;
import lombok.Data;

import java.util.List;

@Data
public class MonthlySummaryResponse {
    private List<Budget> budgetList;
    private double totalPaidInAmount;
    private double totalPaidOutAmount;
    private double totalGrossAmount;
}

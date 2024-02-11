package com.benatt.hfms.data.budget.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BudgetRequest {
    private Double budgetAmount;
    private Integer period; // in months
}

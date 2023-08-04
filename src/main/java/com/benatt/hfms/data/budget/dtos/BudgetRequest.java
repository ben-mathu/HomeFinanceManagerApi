package com.benatt.hfms.data.budget.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BudgetRequest {
    @NotBlank(message = "category must not be blank")
    private String category;
}

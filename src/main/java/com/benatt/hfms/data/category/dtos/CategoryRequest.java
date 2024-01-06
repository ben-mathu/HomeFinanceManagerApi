package com.benatt.hfms.data.category.dtos;

import com.benatt.hfms.data.category.models.CategoryType;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class CategoryRequest {
    private CategoryType categoryType;
    @NotBlank(message = "categoryName must not be bank")
    private String categoryName;
    @Min(value = 0, message = "paidIn must be >=0")
    private double amount = 0;
}

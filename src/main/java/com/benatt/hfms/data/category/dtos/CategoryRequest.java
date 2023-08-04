package com.benatt.hfms.data.category.dtos;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class CategoryRequest {
    @NotBlank(message = "categoryName must not be bank")
    private String categoryName;
    @Min(value = 0, message = "paidIn must be >=0")
    private double paidIn = 0;
    @Min(value = 0, message = "paidIn must be >=0")
    private double paidOut = 0;
}

package com.benatt.hfms.data.accounts.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class AccountRequest {
    @NotBlank(message = "account name must not be blank")
    private String accountName;
    @Min(value = 0, message = "balance must be >= 0")
    private double balance;
}

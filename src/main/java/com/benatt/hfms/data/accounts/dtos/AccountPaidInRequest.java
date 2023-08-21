package com.benatt.hfms.data.accounts.dtos;

import lombok.Data;

@Data
public class AccountPaidInRequest {
    private String paidFrom;
    private double amount;
}

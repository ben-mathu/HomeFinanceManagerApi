package com.benatt.hfms.data.accounts.dtos;

import lombok.Data;

@Data
public class AccountPaidOutRequest {
    private String paidTo;
    private double amount;
    private String transactionDetails;
}

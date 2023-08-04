package com.benatt.hfms.data.wishlist.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WishListRequest {
    @NotBlank(message = "wishListName must not be blank")
    private String wishListName;
    @Min(value = 0, message = "amount must be >= 0")
    private double amount;
    @NotNull(message = "bought must false|true")
    private boolean bought;
}

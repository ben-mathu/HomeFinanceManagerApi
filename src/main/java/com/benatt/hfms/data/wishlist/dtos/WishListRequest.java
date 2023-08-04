package com.benatt.hfms.data.wishlist.dtos;

import lombok.Data;

@Data
public class WishListRequest {
    private String wishListName;
    private double amount;
}

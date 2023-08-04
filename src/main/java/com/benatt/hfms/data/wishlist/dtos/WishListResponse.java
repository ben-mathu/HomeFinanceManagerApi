package com.benatt.hfms.data.wishlist.dtos;

import com.benatt.hfms.data.wishlist.models.WishList;
import lombok.Data;

import java.util.List;

@Data
public class WishListResponse {
    private List<WishList> list;
    private double totalAmount;
}

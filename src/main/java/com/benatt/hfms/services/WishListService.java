package com.benatt.hfms.services;

import com.benatt.hfms.data.wishlist.dtos.WishListRequest;
import com.benatt.hfms.data.wishlist.dtos.WishListResponse;
import com.benatt.hfms.data.wishlist.models.WishList;
import com.benatt.hfms.exceptions.EmptyResultException;

public interface WishListService {
    WishList addWishList(WishListRequest request);

    WishListResponse getWishList() throws EmptyResultException;
}

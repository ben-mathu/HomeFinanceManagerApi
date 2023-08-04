package com.benatt.hfms.services;

import com.benatt.hfms.data.wishlist.dtos.WishListRequest;
import com.benatt.hfms.data.wishlist.dtos.WishListResponse;
import com.benatt.hfms.data.wishlist.models.WishList;
import com.benatt.hfms.exceptions.EmptyResultException;
import org.springframework.http.ResponseEntity;

public interface WishListService {
    WishList addWishList(WishListRequest request);

    WishListResponse getWishList() throws EmptyResultException;
}

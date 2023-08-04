package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.wishlist.WishListRepository;
import com.benatt.hfms.data.wishlist.dtos.WishListRequest;
import com.benatt.hfms.data.wishlist.dtos.WishListResponse;
import com.benatt.hfms.data.wishlist.models.WishList;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListServiceImpl implements WishListService {
    @Autowired
    private WishListRepository wishListRepository;

    @Override
    public WishList addWishList(WishListRequest request) {
        WishList wishList = new WishList();
        wishList.setName(request.getWishListName());
        wishList.setAmount(request.getAmount());
        return wishListRepository.save(wishList);
    }

    @Override
    public WishListResponse getWishList() throws EmptyResultException {
        List<WishList> list = wishListRepository.findAll();

        if (list.isEmpty())
            throw new EmptyResultException("Wish list is empty");

        double totalAmount = 0;
        for (WishList wishList : list) {
            totalAmount += wishList.getAmount();
        }

        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setList(list);
        wishListResponse.setTotalAmount(totalAmount);

        return wishListResponse;
    }
}

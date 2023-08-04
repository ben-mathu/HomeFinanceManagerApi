package com.benatt.hfms.controllers;

import com.benatt.hfms.data.wishlist.dtos.WishListRequest;
import com.benatt.hfms.data.wishlist.dtos.WishListResponse;
import com.benatt.hfms.data.wishlist.models.WishList;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.services.impl.WishListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wish-list")
public class WishListController {
    @Autowired
    private WishListServiceImpl wishListService;

    @PostMapping
    public ResponseEntity<WishList> addWishList(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody WishListRequest request) {
        return ResponseEntity.ok(wishListService.addWishList(request));
    }

    @GetMapping
    public ResponseEntity<WishListResponse> getAllWishList() throws EmptyResultException {
        return ResponseEntity.ok(wishListService.getWishList());
    }
}

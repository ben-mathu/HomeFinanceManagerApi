package com.benatt.hfms.data.wishlist;

import com.benatt.hfms.data.wishlist.models.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}

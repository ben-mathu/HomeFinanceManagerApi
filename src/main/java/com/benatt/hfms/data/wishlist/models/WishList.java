package com.benatt.hfms.data.wishlist.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class WishList {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(unique = true, nullable = false)
    private String name;
    private double amount = 0;
    @Column(nullable = false)
    private boolean bought;
}

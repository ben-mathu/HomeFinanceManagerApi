package com.benatt.hfms.data.wishlist.models;

import com.benatt.hfms.data.accounts.models.Account;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class WishList {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean bought;
    @Column(nullable = false)
    private double amount = 0;
    @JsonManagedReference("account-wishlist")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Account account;
}

package com.benatt.hfms.data.accounts.models;

import com.benatt.hfms.data.budget.models.Budget;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Handles all more than one accounts
 * There should be a specific category to handle expenses or income, which will be
 * committed to other categories, and this will be included when showing account statement
 * instead of Budget statement. It should also affect the balance of the account.
 */
@Data
@Entity
public class Account {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private double balance = 0;
    @OneToMany
    private List<Budget> budgetList;
}

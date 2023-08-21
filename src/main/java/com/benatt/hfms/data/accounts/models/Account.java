package com.benatt.hfms.data.accounts.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles all more than one accounts
 * There should be a specific category to handle expenses or income, which will be
 * committed to other categories, and this will be included when showing account statement
 * instead of Budget statement. It should also affect the balance of the account.
 */

@NoArgsConstructor
@AllArgsConstructor
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
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
}

package com.benatt.hfms.data.accounts.models;

import com.benatt.hfms.data.transactions.models.Transaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private double balance = 0;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @JsonManagedReference("transactions-accounts")
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}

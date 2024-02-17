package com.benatt.hfms.data.transactions.models;

import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.category.models.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Transaction {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String paidTo;
    private double paidOut;
    private String paidFrom;
    private double paidIn;
    @Column(unique = true)
    private String description;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference("transactions-accounts")
    private Account account;
    @ManyToOne
    private Category category;
}

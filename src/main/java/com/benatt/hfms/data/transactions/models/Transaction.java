package com.benatt.hfms.data.transactions.models;

import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.category.models.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

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
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference("transactions-accounts")
    private Account account;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Category category;
}

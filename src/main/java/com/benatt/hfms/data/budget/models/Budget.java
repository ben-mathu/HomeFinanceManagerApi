package com.benatt.hfms.data.budget.models;

import com.benatt.hfms.data.accounts.models.Account;
import com.benatt.hfms.data.category.models.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Budget {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String categoryType;
    @JsonManagedReference("budget-category")
    @OneToMany(mappedBy = "budget", fetch = FetchType.EAGER)
    private List<Category> categories;

    @JsonBackReference("account-budget")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Account account;
}

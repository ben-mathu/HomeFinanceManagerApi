package com.benatt.hfms.data.category.models;

import com.benatt.hfms.data.budget.models.Budget;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Category {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Column(nullable = true, unique = true)
    private String name;
    @Column(nullable = false)
    private double percentage;
    private CategoryType categoryType;
    @JsonManagedReference("budget-category")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Budget budget;
}

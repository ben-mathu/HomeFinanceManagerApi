package com.benatt.hfms.data.category.models;

import com.benatt.hfms.data.budget.models.Budget;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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
    @JsonBackReference("budget-category")
    @ManyToOne
    private Budget budget;
}

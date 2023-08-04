package com.benatt.hfms.data.budget.models;

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
    private String categoryType;
    @OneToMany
    private List<Category> categories;
}

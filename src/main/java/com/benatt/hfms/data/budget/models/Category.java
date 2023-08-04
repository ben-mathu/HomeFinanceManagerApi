package com.benatt.hfms.data.budget.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Category {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name;
    private double paidIn;
    private double paidOut;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Budget budget;
}

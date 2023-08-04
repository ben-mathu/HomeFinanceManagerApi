package com.benatt.hfms.data.category.models;

import com.benatt.hfms.data.budget.models.Budget;
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
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private double paidIn = 0;
    @Column(nullable = false)
    private double paidOut = 0;
    private String transactionId;
    private String details;
    private String paidTo;
    private String paidFrom;
    private Date dateCompleted;
}

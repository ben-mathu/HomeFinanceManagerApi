package com.benatt.hfms.data.budget.models;

import com.benatt.hfms.data.category.models.Category;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Budget {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @JsonManagedReference("budget-category")
    @OneToMany(mappedBy = "budget", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Category.class)
    private List<Category> categories;
    private Integer budgetPeriod;
    private Double amountBudgeted;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
}

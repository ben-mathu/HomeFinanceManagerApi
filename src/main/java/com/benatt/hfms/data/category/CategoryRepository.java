package com.benatt.hfms.data.category;

import com.benatt.hfms.data.category.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByBudgetId(Long budgetId);
}

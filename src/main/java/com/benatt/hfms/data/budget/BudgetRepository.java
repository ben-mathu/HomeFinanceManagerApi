package com.benatt.hfms.data.budget;

import com.benatt.hfms.data.budget.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}

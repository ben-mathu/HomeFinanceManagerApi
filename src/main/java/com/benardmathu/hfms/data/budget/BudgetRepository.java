package com.benardmathu.hfms.data.budget;

import com.benardmathu.hfms.data.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}

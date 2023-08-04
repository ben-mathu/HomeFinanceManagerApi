package com.benardmathu.hfms.data.budget;

import com.benardmathu.hfms.data.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> getBudgetByHouseholdId(Long householdId);
}

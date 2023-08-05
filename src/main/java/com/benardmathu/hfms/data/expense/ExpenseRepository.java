package com.benardmathu.hfms.data.expense;

import com.benardmathu.hfms.data.expense.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByMoneyJarId(Long id);
}

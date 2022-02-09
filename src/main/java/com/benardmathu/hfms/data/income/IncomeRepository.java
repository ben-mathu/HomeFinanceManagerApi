package com.benardmathu.hfms.data.income;

import com.benardmathu.hfms.data.income.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}

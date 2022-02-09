package com.benardmathu.hfms.data.income;

import com.benardmathu.hfms.data.income.model.OnInComeChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeChangeRepository extends JpaRepository<OnInComeChange, Long> {
}

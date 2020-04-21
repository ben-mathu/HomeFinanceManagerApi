package com.benardmathu.hfms.data.income;

import com.benardmathu.hfms.data.income.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    Optional<Income> findByUserId(Long id);
}

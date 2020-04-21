package com.benardmathu.hfms.data.income;

import com.benardmathu.hfms.data.income.model.OnInComeChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface IncomeChangeRepository extends JpaRepository<OnInComeChange, Long> {
    List<OnInComeChange> findByIdAndCreatedAtGreaterThanEqual(Long id, Date from, Date to);
}

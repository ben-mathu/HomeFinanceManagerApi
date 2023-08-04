package com.benardmathu.hfms.data.tablerelationships.jarexpenserel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyJarExpenseRepository extends JpaRepository<MoneyJarExpenseRel, Long> {
    Long findByJarId(Long jarId);
}

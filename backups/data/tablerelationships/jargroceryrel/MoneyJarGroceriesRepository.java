package com.benardmathu.hfms.data.tablerelationships.jargroceryrel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoneyJarGroceriesRepository extends JpaRepository<MoneyJarGroceriesRel, Long> {
    List<String> findByJarId(Long jarId);
}

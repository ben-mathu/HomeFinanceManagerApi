package com.benardmathu.hfms.data.tablerelationships.userhouse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHouseholdRepository extends JpaRepository<UserHouseholdRel, Long> {
    Optional<UserHouseholdRel> findByUserId(Long userId);

    List<UserHouseholdRel> findAllByHouseholdId(Long id);

    List<UserHouseholdRel> findAllByUserId(Long id);
}

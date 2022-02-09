package com.benardmathu.hfms.data.household;

import com.benardmathu.hfms.data.household.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
}

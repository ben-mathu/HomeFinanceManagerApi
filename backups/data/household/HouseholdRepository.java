package com.benardmathu.hfms.data.household;

import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
    Household findByUserId(Long userId);

    List<User> findUserListById(Long houseId);
}

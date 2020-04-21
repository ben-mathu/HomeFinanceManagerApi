package com.benardmathu.hfms.data.user;

import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Household findHouseholdByUserId(Long userId);

    User findByUsername(String username);
}

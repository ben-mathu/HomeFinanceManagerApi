package com.benardmathu.hfms.data.grocery;

import com.benardmathu.hfms.data.grocery.model.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 25/03/2021
 */
public interface GroceryRepository extends JpaRepository<Grocery, Long> {
}

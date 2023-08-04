package com.benatt.hfms.data.category;

import com.benatt.hfms.data.category.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

package com.benatt.hfms.services;

import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;

public interface CategoriesService {

    Category addCategory(CategoryRequest request, Long id);

    Category addPaidOutAmount(Long categoryId, double amount);

    Category addPaidInAmount(Long categoryId, double amount);
}

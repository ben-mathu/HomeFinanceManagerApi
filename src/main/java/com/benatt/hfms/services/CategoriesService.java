package com.benatt.hfms.services;

import com.benatt.hfms.data.category.models.Category;

public interface CategoriesService {

    Category addPaidOutAmount(Long categoryId, double amount);

    Category addPaidInAmount(Long categoryId, double amount);
}

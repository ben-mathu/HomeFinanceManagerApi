package com.benatt.hfms.services.impl;

import com.benatt.hfms.data.category.CategoryRepository;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category addPaidOutAmount(Long categoryId, double amount) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category == null)
            throw new InvalidParameterException("Category with id " + categoryId + " was not found.");

        category.setPaidOut(category.getPaidOut() + amount);

        return categoryRepository.save(category);
    }

    @Override
    public Category addPaidInAmount(Long categoryId, double amount) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category == null)
            throw new InvalidParameterException("Category with id " + categoryId + " was not found.");

        category.setPaidIn(category.getPaidIn() + amount);

        return categoryRepository.save(category);
    }
}

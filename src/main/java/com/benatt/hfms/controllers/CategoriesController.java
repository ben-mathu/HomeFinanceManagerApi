package com.benatt.hfms.controllers;

import com.benatt.hfms.data.category.dtos.CategoryRequest;
import com.benatt.hfms.data.category.models.Category;
import com.benatt.hfms.services.impl.CategoriesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    @Autowired
    private CategoriesServiceImpl categoriesService;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody CategoryRequest request,
                                                @RequestParam("budgetId") Long id) {
        return ResponseEntity.ok(categoriesService.addCategory(request, id));
    }

    @PostMapping("{categoryId}/paidOut")
    public ResponseEntity<Category> addPaidOutAmount(@PathVariable("categoryId") Long categoryId,
                                                     @RequestParam("amount") double amount) {
        return ResponseEntity.ok(categoriesService.addPaidOutAmount(categoryId, amount));
    }

    @PostMapping("{categoryId}/paidIn")
    public ResponseEntity<Category> addPaidInAmount(@PathVariable("categoryId") Long categoryId,
                                                    @RequestParam("amount") double amount) {
        return ResponseEntity.ok(categoriesService.addPaidInAmount(categoryId, amount));
    }
}

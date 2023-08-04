package com.benatt.hfms.controllers;

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

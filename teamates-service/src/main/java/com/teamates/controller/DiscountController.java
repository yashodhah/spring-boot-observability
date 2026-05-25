package com.teamates.controller;

import com.teamates.model.Discount;
import com.teamates.repository.DiscountRepository;
import com.teamates.service.DiscountCalculationService;
import com.teamates.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

// FLAW: God controller – handles discount CRUD, calculation, AND category-based lookups
// FLAW: injects three dependencies (two services + one repository)
// FLAW: @Autowired field injection throughout
@RestController
@RequestMapping("api/v1/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DiscountCalculationService discountCalculationService;

    @Autowired
    private DiscountRepository discountRepository;  // FLAW: direct repo in controller

    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        return ResponseEntity.ok(discountService.createDiscount(discount));
    }

    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        return ResponseEntity.ok(discountRepository.findAll());  // FLAW: direct repo access
    }

    @GetMapping("/active")
    public ResponseEntity<List<Discount>> getActiveDiscounts() {
        // FLAW: duplicated in both DiscountService and DiscountCalculationService
        return ResponseEntity.ok(discountCalculationService.getActiveDiscounts());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Discount>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(discountService.getDiscountsForCategory(category));
    }

    // FLAW: GET endpoint performing a calculation (side-effect risk)
    @GetMapping("/calculate")
    public ResponseEntity<BigDecimal> calculateDiscount(
            @RequestParam BigDecimal amount,
            @RequestParam BigDecimal percentage) {
        // FLAW: directly calling a service without any request validation
        return ResponseEntity.ok(discountService.applyDiscount(amount, percentage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        // FLAW: no check if discount is currently in use
        discountRepository.deleteById(id);  // FLAW: direct repo delete in controller
        return ResponseEntity.noContent().build();
    }
}

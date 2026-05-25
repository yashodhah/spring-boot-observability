package com.teamates.controller;

import com.teamates.model.Product;
import com.teamates.model.Review;
import com.teamates.repository.ProductRepository;
import com.teamates.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLAW: ProductController handles product CRUD AND review lookups (SRP violation)
// FLAW: still uses constructor injection for ProductRepository but not for ReviewService
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductRepository productRepository;

    // FLAW: inconsistent injection (field vs constructor)
    @org.springframework.beans.factory.annotation.Autowired
    private ReviewService reviewService;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // FLAW: review endpoint duplicated here AND in ReviewController
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(id));
    }

    // FLAW: average rating endpoint also duplicated from ReviewController
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getProductRating(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getAverageRating(id));
    }

    // FLAW: DELETE without checking if product is referenced by existing orders
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

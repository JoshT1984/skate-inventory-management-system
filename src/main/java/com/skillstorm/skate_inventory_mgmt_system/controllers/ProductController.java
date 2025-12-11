package com.skillstorm.skate_inventory_mgmt_system.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.skate_inventory_mgmt_system.models.Product;
import com.skillstorm.skate_inventory_mgmt_system.services.ProductService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")  // allow Angular dev server
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable int id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
        // If not found, ProductService throws NoSuchElementException
        // -> handled by GlobalExceptionHandler as 404
    }

    // PARTIAL UPDATE (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Product> productPartialUpdate(
            @PathVariable int id,
            @RequestBody Product updates) {

        if (updates == null) {
            throw new IllegalArgumentException("Empty request body.");
        }

        boolean noFields = updates.getName() == null &&
                updates.getSku() == null &&
                updates.getCategory() == null &&
                updates.getBrand() == null &&
                updates.getDescription() == null;

        if (noFields) {
            throw new IllegalArgumentException("No fields provided to update.");
        }

        Product updated = productService.updateProductPartial(id, updates);
        return ResponseEntity.ok(updated);
        // NoSuchElementException, DuplicateResourceException, IllegalArgumentException
        // all handled by GlobalExceptionHandler.
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
        // If not found, service throws NoSuchElementException -> 404
    }
}

package com.skillstorm.skate_inventory_mgmt_system.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import com.skillstorm.skate_inventory_mgmt_system.models.Product;
import com.skillstorm.skate_inventory_mgmt_system.services.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // http requests handled
@RequestMapping("/products")

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product created = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().header("message", e.getMessage()).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().header("message", e.getMessage()).build();
        }
    }

    // Request
    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        try {
            List<Product> products = productService.findAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .header("message", e.getMessage())
                    .build();
        }
    }

    // Request
    @GetMapping("/id/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable int id) {
        try {
            Product product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .header("message", e.getMessage())
                    .build();
        }
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<Product> productPartialUpdate(
            @PathVariable int id,
            @RequestBody Product updates) {
        try {
            // Validates BEFORE calling service
            if (updates == null) {
                return ResponseEntity.badRequest().header("message", "Empty request body").build();
            }
            // rejects Empty JSON
            boolean noFields = updates.getName() == null &&
                    updates.getSku() == null &&
                    updates.getCategory() == null &&
                    updates.getBrand() == null &&
                    updates.getDescription() == null;

            if (noFields) {
                return ResponseEntity.badRequest()
                        .header("message", "No fields provided to update")
                        .build();
            }

            Product updated = productService.updateProductPartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().header("message", e.getMessage()).build();
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .header("message", e.getMessage())
                    .build();
        }
    }
}

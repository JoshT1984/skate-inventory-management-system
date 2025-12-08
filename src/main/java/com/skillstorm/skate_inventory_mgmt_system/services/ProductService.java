package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.skillstorm.skate_inventory_mgmt_system.models.Product;
import com.skillstorm.skate_inventory_mgmt_system.repositories.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product request body cannot be null.");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (product.getSku() == null || product.getSku().isBlank()) {
            throw new IllegalArgumentException("Product SKU is required.");
        }
        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("Product category is required.");
        }
        if (product.getBrand() == null || product.getBrand().isBlank()) {
            throw new IllegalArgumentException("Product brand is required.");
        }
        if (product.getDescription() == null || product.getDescription().isBlank()) {
            throw new IllegalArgumentException("Product description is required.");
        }
        // If validation passes, save the product
        return productRepository.save(product);
    }

    // Request
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    // Request
    public Product findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));
    }

    // Partial or Complete Update
    public Product updateProductPartial(int id, Product updates) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

        // Validation checks
        if (updates.getName() != null) {
            existing.setName(updates.getName());
        }

        if (updates.getSku() != null) {
            existing.setSku(updates.getSku());
        }

        if (updates.getCategory() != null) {
            existing.setCategory(updates.getCategory());
        }

        if (updates.getBrand() != null) {
            existing.setBrand(updates.getBrand());
        }

        if (updates.getDescription() != null) {
            existing.setDescription(updates.getDescription());
        }
        return productRepository.save(existing);
    }

    public void deleteProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product was not found by id: " + id));
        productRepository.delete(product);
    }

}

package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.skate_inventory_mgmt_system.api.DuplicateResourceException;
import com.skillstorm.skate_inventory_mgmt_system.models.Product;
import com.skillstorm.skate_inventory_mgmt_system.repositories.ProductRepository;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseInventoryRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    public ProductService(ProductRepository productRepository,
            WarehouseInventoryRepository warehouseInventoryRepository) {
        this.productRepository = productRepository;
        this.warehouseInventoryRepository = warehouseInventoryRepository;
    }

    // Create
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product request body cannot be null.");
        }

        if (productRepository.existsBySku(product.getSku())) {
            throw new DuplicateResourceException("Product SKU must be unique.");
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
        if (updates.getName() != null && !updates.getName().isBlank()) {
            existing.setName(updates.getName());
        }

        if (updates.getSku() != null) {
            // Only check if the SKU is actually changing
            if (!updates.getSku().equals(existing.getSku())
                    && productRepository.existsBySku(updates.getSku())) {
                throw new DuplicateResourceException("Product SKU must be unique.");
            }
            existing.setSku(updates.getSku());
        }

        if (updates.getCategory() != null && !updates.getCategory().isBlank()) {
            existing.setCategory(updates.getCategory());
        }

        if (updates.getBrand() != null && !updates.getBrand().isBlank()) {
            existing.setBrand(updates.getBrand());
        }

        if (updates.getDescription() != null && !updates.getDescription().isBlank()) {
            existing.setDescription(updates.getDescription());
        }

        return productRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteProductById(int id) {
        // Load the product (or fail if not found)
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Product was not found by id: " + id));

        // Delete all warehouse_inventory rows that reference this product
        warehouseInventoryRepository.deleteByProduct(product);

        // Delete the product itself
        productRepository.delete(product);
    }
}

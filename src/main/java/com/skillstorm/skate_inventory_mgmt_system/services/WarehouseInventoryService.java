package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.skillstorm.skate_inventory_mgmt_system.api.DuplicateResourceException;
import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseInventoryRepository;

@Service
public class WarehouseInventoryService {

    private final WarehouseInventoryRepository warehouseInventoryRepository;

    public WarehouseInventoryService(WarehouseInventoryRepository warehouseInventoryRepository) {
        this.warehouseInventoryRepository = warehouseInventoryRepository;
    }

    // CREATE
    public WarehouseInventory createWarehouseInventory(WarehouseInventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Warehouse inventory request body cannot be null.");
        }

        // Enforce many-to-many uniqueness (warehouse_id, product_id)
        if (warehouseInventoryRepository.existsByWarehouseIdAndProductId(
                inventory.getWarehouseId(),
                inventory.getProductId())) {
            throw new DuplicateResourceException(
                    "This warehouse already has an inventory entry for the given product.");
        }

        // Basic business rule (Bean Validation also enforces @Min(0))
        if (inventory.getQuantity() != null && inventory.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        return warehouseInventoryRepository.save(inventory);
    }

    // READ ALL
    public List<WarehouseInventory> findAllInventory() {
        return warehouseInventoryRepository.findAll();
    }

    // READ ONE BY ID
    public WarehouseInventory findById(int id) {
        return warehouseInventoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Warehouse inventory not found with id " + id));
    }

    // READ BY WAREHOUSE + PRODUCT (optional helper)
    public WarehouseInventory findByWarehouseAndProduct(int warehouseId, int productId) {
        return warehouseInventoryRepository.findByWarehouseIdAndProductId(warehouseId, productId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Warehouse inventory not found for warehouseId " + warehouseId
                                + " and productId " + productId));
    }

    // PARTIAL OR COMPLETE UPDATE (PATCH-style)
    public WarehouseInventory updateWarehouseInventoryPartial(int id, WarehouseInventory updates) {
        WarehouseInventory existing = warehouseInventoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Warehouse inventory not found with id " + id));

        if (updates == null) {
            return existing; // no-op
        }

        // Handle potential changes in warehouseId/productId pair
        Integer newWarehouseId = updates.getWarehouseId() != null
                ? updates.getWarehouseId()
                : existing.getWarehouseId();

        Integer newProductId = updates.getProductId() != null
                ? updates.getProductId()
                : existing.getProductId();

        boolean pairChanged = !newWarehouseId.equals(existing.getWarehouseId())
                || !newProductId.equals(existing.getProductId());

        if (pairChanged && warehouseInventoryRepository.existsByWarehouseIdAndProductId(
                newWarehouseId, newProductId)) {
            throw new DuplicateResourceException(
                    "This warehouse already has an inventory entry for the given product.");
        }

        // Apply updates
        if (updates.getWarehouseId() != null) {
            existing.setWarehouseId(updates.getWarehouseId());
        }
        if (updates.getProductId() != null) {
            existing.setProductId(updates.getProductId());
        }
        if (updates.getQuantity() != null) {
            existing.setQuantity(updates.getQuantity());
        }
        if (updates.getStorageLocation() != null && !updates.getStorageLocation().isBlank()) {
            existing.setStorageLocation(updates.getStorageLocation());
        }

        // Re-check business rule
        if (existing.getQuantity() != null && existing.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        return warehouseInventoryRepository.save(existing);
    }

    // DELETE
    public void deleteWarehouseInventoryById(int id) {
        WarehouseInventory existing = warehouseInventoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Warehouse inventory was not found by id: " + id));
        warehouseInventoryRepository.delete(existing);
    }
}

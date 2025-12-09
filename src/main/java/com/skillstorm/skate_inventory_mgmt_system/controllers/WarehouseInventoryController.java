package com.skillstorm.skate_inventory_mgmt_system.controllers;

import java.util.List;

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

import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;
import com.skillstorm.skate_inventory_mgmt_system.services.WarehouseInventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/warehouse-inventory")
public class WarehouseInventoryController {

    private final WarehouseInventoryService warehouseInventoryService;

    public WarehouseInventoryController(WarehouseInventoryService warehouseInventoryService) {
        this.warehouseInventoryService = warehouseInventoryService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<WarehouseInventory> createWarehouseInventory(
            @Valid @RequestBody WarehouseInventory inventory) {

        WarehouseInventory created = warehouseInventoryService.createWarehouseInventory(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        // Validation errors -> MethodArgumentNotValidException -> 400 via
        // GlobalExceptionHandler
        // DuplicateResourceException / IllegalArgumentException -> handled globally
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<WarehouseInventory>> findAllInventory() {
        List<WarehouseInventory> warehouseInventory = warehouseInventoryService.findAllInventory();
        return ResponseEntity.ok(warehouseInventory);
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseInventory> findInventoryById(@PathVariable int id) {
        WarehouseInventory inventory = warehouseInventoryService.findById(id);
        return ResponseEntity.ok(inventory);
        // If not found -> NoSuchElementException -> 404 via GlobalExceptionHandler
    }

    // OPTIONAL: READ BY WAREHOUSE + PRODUCT
    @GetMapping("/warehouse/{warehouseId}/product/{productId}")
    public ResponseEntity<WarehouseInventory> findByWarehouseAndProduct(
            @PathVariable int warehouseId,
            @PathVariable int productId) {

        WarehouseInventory inventory = warehouseInventoryService.findByWarehouseAndProduct(warehouseId, productId);
        return ResponseEntity.ok(inventory);
    }

    // PARTIAL UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<WarehouseInventory> warehouseInventoryPartialUpdate(
            @PathVariable int id,
            @RequestBody WarehouseInventory updates) {

        if (updates == null) {
            throw new IllegalArgumentException("Empty request body.");
        }

        boolean noFields = updates.getWarehouseId() == null &&
                updates.getProductId() == null &&
                updates.getQuantity() == null &&
                updates.getStorageLocation() == null;

        if (noFields) {
            throw new IllegalArgumentException("No fields provided to update.");
        }

        WarehouseInventory updated = warehouseInventoryService.updateWarehouseInventoryPartial(id, updates);
        return ResponseEntity.ok(updated);
        // Exceptions bubble to GlobalExceptionHandler
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseInventory(@PathVariable int id) {
        warehouseInventoryService.deleteWarehouseInventoryById(id);
        return ResponseEntity.noContent().build();
        // If not found -> NoSuchElementException -> 404 via GlobalExceptionHandler
    }
}

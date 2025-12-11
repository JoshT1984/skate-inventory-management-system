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

import com.skillstorm.skate_inventory_mgmt_system.models.Warehouse;
import com.skillstorm.skate_inventory_mgmt_system.services.WarehouseService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")  // allow Angular dev server
@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        Warehouse created = warehouseService.createWarehouse(warehouse);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        // Validation errors -> MethodArgumentNotValidException -> handled globally (400)
        // DuplicateResourceException / IllegalArgumentException -> handled globally
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Warehouse>> findAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.findAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> findWarehouseById(@PathVariable int id) {
        Warehouse warehouse = warehouseService.findById(id);
        return ResponseEntity.ok(warehouse);
        // If not found, service throws NoSuchElementException -> 404 via GlobalExceptionHandler
    }

    // PARTIAL UPDATE (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Warehouse> warehousePartialUpdate(
            @PathVariable int id,
            @RequestBody Warehouse updates) {

        if (updates == null) {
            throw new IllegalArgumentException("Empty request body.");
        }

        boolean noFields =
                updates.getName() == null &&
                updates.getLocation() == null &&
                updates.getAddress() == null &&
                updates.getCode() == null &&
                updates.getMaxCapacity() == null &&
                updates.getCurrentCapacity() == null &&
                updates.getIsActive() == null;

        if (noFields) {
            throw new IllegalArgumentException("No fields provided to update.");
        }

        Warehouse updated = warehouseService.updateWarehousePartial(id, updates);
        return ResponseEntity.ok(updated);
        // Any exceptions (not found, duplicate, illegal args) bubble to GlobalExceptionHandler
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable int id) {
        warehouseService.deleteWarehouseById(id);
        return ResponseEntity.noContent().build();
        // If not found -> NoSuchElementException -> 404 via GlobalExceptionHandler
    }
}

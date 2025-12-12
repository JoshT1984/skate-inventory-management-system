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

import com.skillstorm.skate_inventory_mgmt_system.dtos.WarehouseInventoryRequest;
import com.skillstorm.skate_inventory_mgmt_system.dtos.WarehouseInventoryResponse;
import com.skillstorm.skate_inventory_mgmt_system.dtos.WarehouseInventoryTransferRequest;
import com.skillstorm.skate_inventory_mgmt_system.services.WarehouseInventoryService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/warehouse-inventory")
public class WarehouseInventoryController {

    private final WarehouseInventoryService warehouseInventoryService;

    public WarehouseInventoryController(WarehouseInventoryService warehouseInventoryService) {
        this.warehouseInventoryService = warehouseInventoryService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<WarehouseInventoryResponse> createWarehouseInventory(
            @Valid @RequestBody WarehouseInventoryRequest request) {

        WarehouseInventoryResponse created = warehouseInventoryService.createWarehouseInventory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // CREATE/TRANSFER
    @PostMapping("/transfer")
    public void transfer(@RequestBody WarehouseInventoryTransferRequest dto) {
        warehouseInventoryService.transferInventory(dto);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<WarehouseInventoryResponse>> findAllInventory() {
        return ResponseEntity.ok(warehouseInventoryService.findAllInventory());
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseInventoryResponse> findInventoryById(@PathVariable int id) {
        return ResponseEntity.ok(warehouseInventoryService.findById(id));
    }

    // READ BY WAREHOUSE + PRODUCT
    @GetMapping("/warehouse/{warehouseId}/product/{productId}")
    public ResponseEntity<WarehouseInventoryResponse> findByWarehouseAndProduct(
            @PathVariable int warehouseId,
            @PathVariable int productId) {

        return ResponseEntity.ok(
                warehouseInventoryService.findByWarehouseAndProduct(warehouseId, productId));
    }

    // PARTIAL UPDATE (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<WarehouseInventoryResponse> warehouseInventoryPartialUpdate(
            @PathVariable int id,
            @RequestBody WarehouseInventoryRequest updates) {

        boolean noFields = updates == null
                || (updates.warehouseId == null
                        && updates.productId == null
                        && updates.quantity == null
                        && (updates.storageLocation == null
                                || updates.storageLocation.isBlank()));

        if (noFields) {
            throw new IllegalArgumentException("No fields provided to update.");
        }

        WarehouseInventoryResponse updated = warehouseInventoryService.updateWarehouseInventoryPartial(id, updates);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseInventory(@PathVariable int id) {
        warehouseInventoryService.deleteWarehouseInventoryById(id);
        return ResponseEntity.noContent().build();
    }
}

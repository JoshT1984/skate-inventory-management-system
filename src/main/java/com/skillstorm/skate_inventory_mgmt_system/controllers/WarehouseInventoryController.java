package com.skillstorm.skate_inventory_mgmt_system.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;
import com.skillstorm.skate_inventory_mgmt_system.services.WarehouseInventoryService;

@RestController
@RequestMapping("warehouse-inventory")

public class WarehouseInventoryController {

    private final WarehouseInventoryService warehouseInventoryService;

    public WarehouseInventoryController(WarehouseInventoryService warehouseInventoryService) {
        this.warehouseInventoryService = warehouseInventoryService;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseInventory>> findAllWarehouses() {
        try {
            List<WarehouseInventory> warehouseInventory = warehouseInventoryService.findAllWarehouses();
            return new ResponseEntity<>(warehouseInventory, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().header("message", e.getMessage()).build();
        }
    }

}

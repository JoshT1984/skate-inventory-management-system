package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseInventoryRepository;

@Service
public class WarehouseInventoryService {
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    public WarehouseInventoryService(WarehouseInventoryRepository warehouseInventoryRepository) {
        this.warehouseInventoryRepository = warehouseInventoryRepository;
    }

    public List<WarehouseInventory> findAllWarehouses() {
        return warehouseInventoryRepository.findAll();
    }

}

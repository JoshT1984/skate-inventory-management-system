package com.skillstorm.skate_inventory_mgmt_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;

public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Integer> {

}

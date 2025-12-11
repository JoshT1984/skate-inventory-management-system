package com.skillstorm.skate_inventory_mgmt_system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;

@Repository
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Integer> {

    boolean existsByWarehouseIdAndProductId(Integer warehouseId, Integer productId);

    Optional<WarehouseInventory> findByWarehouseIdAndProductId(Integer warehouseId, Integer productId);

    List<WarehouseInventory> findByWarehouseId(Integer warehouseId);

    List<WarehouseInventory> findByProductId(Integer productId);

    // delete all inventory rows for a given product
    void deleteByProductId(Integer productId);
}

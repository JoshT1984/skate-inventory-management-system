package com.skillstorm.skate_inventory_mgmt_system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.skate_inventory_mgmt_system.models.Product;
import com.skillstorm.skate_inventory_mgmt_system.models.Warehouse;
import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;

@Repository
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Integer> {

    boolean existsByWarehouseAndProduct(Warehouse warehouse, Product product);

    Optional<WarehouseInventory> findByWarehouseAndProduct(Warehouse warehouse, Product product);

    List<WarehouseInventory> findByWarehouse(Warehouse warehouse);

    List<WarehouseInventory> findByProduct(Product product);

    // delete all inventory rows for a given product
    void deleteByProduct(Product product);
}
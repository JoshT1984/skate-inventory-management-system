package com.skillstorm.skate_inventory_mgmt_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.skate_inventory_mgmt_system.models.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

    boolean existsByCode(String code);

    boolean existsByNameAndLocation(String name, String location);
}

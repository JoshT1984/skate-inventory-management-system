package com.skillstorm.skate_inventory_mgmt_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.skate_inventory_mgmt_system.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsBySku(String sku);
}

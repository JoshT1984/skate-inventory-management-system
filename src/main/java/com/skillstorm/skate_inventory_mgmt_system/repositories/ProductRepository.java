package com.skillstorm.skate_inventory_mgmt_system.repositories;

import com.skillstorm.skate_inventory_mgmt_system.models.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}

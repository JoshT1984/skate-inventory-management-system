package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.skate_inventory_mgmt_system.api.DuplicateResourceException;
import com.skillstorm.skate_inventory_mgmt_system.models.Warehouse;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseInventoryRepository;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    public WarehouseService(WarehouseRepository warehouseRepository,
            WarehouseInventoryRepository warehouseInventoryRepository) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseInventoryRepository = warehouseInventoryRepository;
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse request body cannot be null.");
        }
        if (warehouseRepository.existsByCode(warehouse.getCode())) {
            throw new DuplicateResourceException("Warehouse code must be unique.");
        }
        if (warehouseRepository.existsByNameAndLocation(warehouse.getName(), warehouse.getLocation())) {
            throw new DuplicateResourceException("A warehouse with the same name and location already exists.");
        }
        if (warehouse.getCurrentCapacity() > warehouse.getMaxCapacity()) {
            throw new IllegalArgumentException("Current capacity cannot exceed max capacity.");
        }
        return warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse findById(int id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warehouse not found with id " + id));
    }

    public Warehouse updateWarehousePartial(int id, Warehouse updates) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warehouse not found with id " + id));

        if (updates == null) {
            return existing;
        }

        String proposedName = existing.getName();
        String proposedLocation = existing.getLocation();

        if (updates.getName() != null && !updates.getName().isBlank()) {
            proposedName = updates.getName();
            existing.setName(updates.getName());
        }
        if (updates.getLocation() != null && !updates.getLocation().isBlank()) {
            proposedLocation = updates.getLocation();
            existing.setLocation(updates.getLocation());
        }
        if (updates.getAddress() != null && !updates.getAddress().isBlank()) {
            existing.setAddress(updates.getAddress());
        }
        if (updates.getCode() != null && !updates.getCode().isBlank()) {
            if (!updates.getCode().equals(existing.getCode()) && warehouseRepository.existsByCode(updates.getCode())) {
                throw new DuplicateResourceException("Warehouse code must be unique.");
            }
            existing.setCode(updates.getCode());
        }
        if (updates.getMaxCapacity() != null) {
            existing.setMaxCapacity(updates.getMaxCapacity());
        }
        if (updates.getCurrentCapacity() != null) {
            existing.setCurrentCapacity(updates.getCurrentCapacity());
        }
        if (updates.getIsActive() != null) {
            existing.setIsActive(updates.getIsActive());
        }

        boolean uniquePairTaken = warehouseRepository.existsByNameAndLocation(proposedName, proposedLocation)
                && (!proposedName.equals(existing.getName()) || !proposedLocation.equals(existing.getLocation()));
        if (uniquePairTaken) {
            throw new DuplicateResourceException("A warehouse with the same name and location already exists.");
        }
        if (existing.getCurrentCapacity() > existing.getMaxCapacity()) {
            throw new IllegalArgumentException("Current capacity cannot exceed max capacity.");
        }
        return warehouseRepository.save(existing);
    }

    @Transactional
    public void deleteWarehouseById(int id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warehouse was not found by id: " + id));
        if (!warehouseInventoryRepository.findByWarehouse(warehouse).isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a warehouse that still has inventory records.");
        }
        warehouseRepository.delete(warehouse);
    }
}

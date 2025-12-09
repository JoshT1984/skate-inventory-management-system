package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.skillstorm.skate_inventory_mgmt_system.api.DuplicateResourceException;
import com.skillstorm.skate_inventory_mgmt_system.models.Warehouse;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    // CREATE
    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse request body cannot be null.");
        }

        // Uniqueness checks (code)
        if (warehouseRepository.existsByCode(warehouse.getCode())) {
            throw new DuplicateResourceException("Warehouse code must be unique.");
        }

        if (warehouseRepository.existsByNameAndLocation(warehouse.getName(), warehouse.getLocation())) {
            throw new DuplicateResourceException(
                    "A warehouse with the same name and location already exists.");
        }

        // Cross-field business rule: capacity relationship
        if (warehouse.getCurrentCapacity() > warehouse.getMaxCapacity()) {
            throw new IllegalArgumentException("Current capacity cannot exceed max capacity.");
        }

        return warehouseRepository.save(warehouse);
    }

    // READ ALL
    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    // READ ONE
    public Warehouse findById(int id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warehouse not found with id " + id));
    }

    // PARTIAL OR COMPLETE UPDATE (PATCH)
    public Warehouse updateWarehousePartial(int id, Warehouse updates) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warehouse not found with id " + id));

        if (updates == null) {
            return existing; // no-op; controller ideally prevents this
        }

        // Strings – update only when non-null and non-blank
        if (updates.getName() != null && !updates.getName().isBlank()) {
            existing.setName(updates.getName());
        }
        if (updates.getLocation() != null && !updates.getLocation().isBlank()) {
            existing.setLocation(updates.getLocation());
        }
        if (updates.getAddress() != null && !updates.getAddress().isBlank()) {
            existing.setAddress(updates.getAddress());
        }
        if (updates.getCode() != null && !updates.getCode().isBlank()) {
            // Check code uniqueness only if it actually changes
            if (!updates.getCode().equals(existing.getCode())
                    && warehouseRepository.existsByCode(updates.getCode())) {
                throw new DuplicateResourceException("Warehouse code must be unique.");
            }
            existing.setCode(updates.getCode());
        }

        // Numeric fields – update when non-null
        if (updates.getMaxCapacity() != null) {
            existing.setMaxCapacity(updates.getMaxCapacity());
        }
        if (updates.getCurrentCapacity() != null) {
            existing.setCurrentCapacity(updates.getCurrentCapacity());
        }

        // Boolean – update when non-null
        if (updates.getIsActive() != null) {
            existing.setIsActive(updates.getIsActive());
        }

        // Enforce unique name+location after updates
        String effectiveName = existing.getName();
        String effectiveLocation = existing.getLocation();
        boolean nameOrLocationChanged = (updates.getName() != null && !updates.getName().isBlank() &&
                !updates.getName().equals(existing.getName()))
                || (updates.getLocation() != null && !updates.getLocation().isBlank() &&
                        !updates.getLocation().equals(existing.getLocation()));

        if (nameOrLocationChanged &&
                warehouseRepository.existsByNameAndLocation(effectiveName, effectiveLocation)) {
            throw new DuplicateResourceException(
                    "A warehouse with the same name and location already exists.");
        }

        // Re-validate capacity constraint after patch
        if (existing.getCurrentCapacity() > existing.getMaxCapacity()) {
            throw new IllegalArgumentException("Current capacity cannot exceed max capacity.");
        }

        return warehouseRepository.save(existing);
    }

    // DELETE
    public void deleteWarehouseById(int id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Warehouse was not found by id: " + id));
        warehouseRepository.delete(warehouse);
    }
}

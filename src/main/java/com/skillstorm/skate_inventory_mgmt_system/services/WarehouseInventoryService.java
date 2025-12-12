package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.skate_inventory_mgmt_system.api.DuplicateResourceException;
import com.skillstorm.skate_inventory_mgmt_system.dtos.WarehouseInventoryRequest;
import com.skillstorm.skate_inventory_mgmt_system.dtos.WarehouseInventoryResponse;
import com.skillstorm.skate_inventory_mgmt_system.dtos.WarehouseInventoryTransferRequest;
import com.skillstorm.skate_inventory_mgmt_system.models.Product;
import com.skillstorm.skate_inventory_mgmt_system.models.Warehouse;
import com.skillstorm.skate_inventory_mgmt_system.models.WarehouseInventory;
import com.skillstorm.skate_inventory_mgmt_system.repositories.ProductRepository;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseInventoryRepository;
import com.skillstorm.skate_inventory_mgmt_system.repositories.WarehouseRepository;

@Service
public class WarehouseInventoryService {

        private final WarehouseInventoryRepository warehouseInventoryRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;

        public WarehouseInventoryService(WarehouseInventoryRepository warehouseInventoryRepository,
                        WarehouseRepository warehouseRepository,
                        ProductRepository productRepository) {
                this.warehouseInventoryRepository = warehouseInventoryRepository;
                this.warehouseRepository = warehouseRepository;
                this.productRepository = productRepository;
        }

        private int safeInt(Integer value) {
                return value != null ? value : 0;
        }

        private void adjustWarehouseCapacity(Warehouse warehouse, int delta) {
                int current = safeInt(warehouse.getCurrentCapacity());
                int max = safeInt(warehouse.getMaxCapacity());
                int updated = current + delta;

                if (updated < 0) {
                        throw new IllegalArgumentException(
                                        "Warehouse capacity cannot go below 0 for warehouse id "
                                                        + warehouse.getWarehouseId());
                }

                if (updated > max) {
                        throw new IllegalArgumentException(
                                        "Operation would exceed max capacity of "
                                                        + max + " units for warehouse id "
                                                        + warehouse.getWarehouseId());
                }

                warehouse.setCurrentCapacity(updated);
                warehouseRepository.save(warehouse);
        }

        // --------- MAPPING HELPERS ---------
        private WarehouseInventoryResponse toResponse(WarehouseInventory entity) {
                return new WarehouseInventoryResponse(
                                entity.getWarehouseInventoryId(),
                                entity.getWarehouse() != null ? entity.getWarehouse().getWarehouseId() : null,
                                entity.getProduct() != null ? entity.getProduct().getProductId() : null,
                                entity.getQuantity(),
                                entity.getStorageLocation());
        }

        // --------- CREATE ---------
        @Transactional
        public WarehouseInventoryResponse createWarehouseInventory(WarehouseInventoryRequest dto) {
                if (dto == null) {
                        throw new IllegalArgumentException("Warehouse inventory request body cannot be null.");
                }

                Warehouse warehouse = warehouseRepository.findById(dto.warehouseId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Warehouse not found with id " + dto.warehouseId));

                Product product = productRepository.findById(dto.productId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Product not found with id " + dto.productId));

                if (warehouseInventoryRepository.existsByWarehouseAndProduct(warehouse, product)) {
                        throw new DuplicateResourceException(
                                        "This warehouse already has an inventory entry for the given product.");
                }

                if (dto.quantity != null && dto.quantity < 0) {
                        throw new IllegalArgumentException("Quantity cannot be negative.");
                }

                int qtyToAdd = safeInt(dto.quantity);

                // capacity enforcement
                adjustWarehouseCapacity(warehouse, qtyToAdd);

                WarehouseInventory entity = new WarehouseInventory(
                                warehouse,
                                product,
                                dto.quantity,
                                dto.storageLocation);

                WarehouseInventory saved = warehouseInventoryRepository.save(entity);
                return toResponse(saved);
        }

        // --------- READ ---------
        public List<WarehouseInventoryResponse> findAllInventory() {
                return warehouseInventoryRepository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .collect(Collectors.toList());
        }

        public WarehouseInventoryResponse findById(int id) {
                WarehouseInventory entity = warehouseInventoryRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Warehouse inventory not found with id " + id));
                return toResponse(entity);
        }

        public WarehouseInventoryResponse findByWarehouseAndProduct(int warehouseId, int productId) {
                Warehouse warehouse = warehouseRepository.findById(warehouseId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Warehouse not found with id " + warehouseId));
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Product not found with id " + productId));

                WarehouseInventory entity = warehouseInventoryRepository.findByWarehouseAndProduct(warehouse, product)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Warehouse inventory not found for warehouseId " + warehouseId
                                                                + " and productId " + productId));

                return toResponse(entity);
        }

        // --------- PATCH / UPDATE ---------
        @Transactional
        public WarehouseInventoryResponse updateWarehouseInventoryPartial(int id, WarehouseInventoryRequest updates) {
                WarehouseInventory existing = warehouseInventoryRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Warehouse inventory not found with id " + id));

                if (updates == null) {
                        return toResponse(existing);
                }

                Warehouse oldWarehouse = existing.getWarehouse();
                Product oldProduct = existing.getProduct();
                Warehouse newWarehouse = oldWarehouse;
                Product newProduct = oldProduct;

                // optional: change warehouse
                if (updates.warehouseId != null) {
                        newWarehouse = warehouseRepository.findById(updates.warehouseId)
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "Warehouse not found with id " + updates.warehouseId));
                }

                // optional: change product
                if (updates.productId != null) {
                        newProduct = productRepository.findById(updates.productId)
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "Product not found with id " + updates.productId));
                }

                boolean pairChanged = !newWarehouse.getWarehouseId().equals(oldWarehouse.getWarehouseId())
                                || !newProduct.getProductId().equals(oldProduct.getProductId());

                if (pairChanged && warehouseInventoryRepository.existsByWarehouseAndProduct(newWarehouse, newProduct)) {
                        throw new DuplicateResourceException(
                                        "This warehouse already has an inventory entry for the given product.");
                }

                int oldQty = safeInt(existing.getQuantity());
                int newQty = oldQty;

                // optional: change quantity
                if (updates.quantity != null) {
                        if (updates.quantity < 0) {
                                throw new IllegalArgumentException("Quantity cannot be negative.");
                        }
                        newQty = updates.quantity;
                }

                // ----- CAPACITY ADJUSTMENTS -----
                if (!newWarehouse.getWarehouseId().equals(oldWarehouse.getWarehouseId())) {
                        // moved inventory to a different warehouse
                        adjustWarehouseCapacity(oldWarehouse, -oldQty); // subtract from old
                        adjustWarehouseCapacity(newWarehouse, newQty); // add to new
                } else {
                        // same warehouse; adjust by delta
                        int delta = newQty - oldQty;
                        if (delta != 0) {
                                adjustWarehouseCapacity(newWarehouse, delta);
                        }
                }
                // ----- END CAPACITY ADJUSTMENTS -----

                // Apply changes to entity
                existing.setWarehouse(newWarehouse);
                existing.setProduct(newProduct);
                existing.setQuantity(newQty);

                if (updates.storageLocation != null && !updates.storageLocation.isBlank()) {
                        existing.setStorageLocation(updates.storageLocation);
                }

                WarehouseInventory saved = warehouseInventoryRepository.save(existing);
                return toResponse(saved);
        }

        // --------- DELETE ---------
        @Transactional
        public void deleteWarehouseInventoryById(int id) {
                WarehouseInventory existing = warehouseInventoryRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Warehouse inventory was not found by id: " + id));

                Warehouse warehouse = existing.getWarehouse();
                int qty = safeInt(existing.getQuantity());

                // decrease capacity first
                adjustWarehouseCapacity(warehouse, -qty);

                warehouseInventoryRepository.delete(existing);
        }

        @Transactional
        public void transferInventory(WarehouseInventoryTransferRequest dto) {

                if (dto == null || dto.productId == null || dto.sourceWarehouseId == null
                                || dto.destinationWarehouseId == null || dto.quantity == null) {
                        throw new IllegalArgumentException("All fields are required for transfer.");
                }

                if (dto.quantity <= 0) {
                        throw new IllegalArgumentException("Transfer quantity must be greater than zero.");
                }

                if (dto.sourceWarehouseId.equals(dto.destinationWarehouseId)) {
                        throw new IllegalArgumentException("Source and destination warehouses must be different.");
                }

                // 1) Load warehouses
                Warehouse sourceWarehouse = warehouseRepository.findById(dto.sourceWarehouseId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Source warehouse not found with id " + dto.sourceWarehouseId));

                Warehouse destWarehouse = warehouseRepository.findById(dto.destinationWarehouseId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Destination warehouse not found with id "
                                                                + dto.destinationWarehouseId));

                // 2) Load product ✅ THIS IS THE 'product' VARIABLE
                Product product = productRepository.findById(dto.productId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Product not found with id " + dto.productId));

                int transferQty = dto.quantity;

                // 3) Find source inventory entry for (sourceWarehouse, product)
                WarehouseInventory sourceInventory = warehouseInventoryRepository
                                .findByWarehouseAndProduct(sourceWarehouse, product)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Product " + dto.productId + " not found in source warehouse "
                                                                + dto.sourceWarehouseId));

                if (sourceInventory.getQuantity() == null || sourceInventory.getQuantity() < transferQty) {
                        throw new IllegalArgumentException("Not enough quantity in source warehouse for transfer.");
                }

                // 4) Find or create destination inventory entry for (destWarehouse, product)
                WarehouseInventory destInventory = warehouseInventoryRepository
                                .findByWarehouseAndProduct(destWarehouse, product)
                                .orElseGet(() -> {
                                        WarehouseInventory inv = new WarehouseInventory();
                                        inv.setWarehouse(destWarehouse);
                                        inv.setProduct(product); 
                                                      
                                        inv.setQuantity(0);
                                        inv.setStorageLocation(sourceInventory.getStorageLocation());
                                        return inv;
                                });

                // 5) Update quantities
                int newSourceQty = sourceInventory.getQuantity() - transferQty;
                sourceInventory.setQuantity(newSourceQty);

                int destCurrent = destInventory.getQuantity() == null ? 0 : destInventory.getQuantity();
                destInventory.setQuantity(destCurrent + transferQty);

                // 6) Update warehouse capacities using your existing helper
                adjustWarehouseCapacity(sourceWarehouse, -transferQty);
                adjustWarehouseCapacity(destWarehouse, transferQty);

                // 7) Save inventory rows
                warehouseInventoryRepository.save(sourceInventory);
                warehouseInventoryRepository.save(destInventory);
        }

}

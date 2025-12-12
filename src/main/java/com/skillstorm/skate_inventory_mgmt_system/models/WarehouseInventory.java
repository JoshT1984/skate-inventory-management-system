package com.skillstorm.skate_inventory_mgmt_system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "WAREHOUSE_INVENTORIES", uniqueConstraints = @UniqueConstraint(columnNames = { "warehouse_id",
        "product_id" }))
public class WarehouseInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_inventory_id", nullable = false)
    private Integer warehouseInventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(0)
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank
    @Size(max = 50)
    @Column(name = "storage_location", nullable = false, length = 50)
    private String storageLocation;

    public WarehouseInventory() {
        // Required by JPA
    }

    public WarehouseInventory(Warehouse warehouse,
            Product product,
            Integer quantity,
            String storageLocation) {
        this.warehouse = warehouse;
        this.product = product;
        this.quantity = quantity;
        this.storageLocation = storageLocation;
    }

    public Integer getWarehouseInventoryId() {
        return warehouseInventoryId;
    }

    public void setWarehouseInventoryId(Integer warehouseInventoryId) {
        this.warehouseInventoryId = warehouseInventoryId;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Override
    public String toString() {
        return "WarehouseInventory [warehouseInventoryId=" + warehouseInventoryId
                + ", warehouseId=" + (warehouse != null ? warehouse.getWarehouseId() : null)
                + ", productId=" + (product != null ? product.getProductId() : null)
                + ", quantity=" + quantity
                + ", storageLocation=" + storageLocation + "]";
    }
}

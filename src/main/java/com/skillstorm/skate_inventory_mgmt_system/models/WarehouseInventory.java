package com.skillstorm.skate_inventory_mgmt_system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "WAREHOUSE_INVENTORY", uniqueConstraints = @UniqueConstraint(columnNames = { "warehouse_id",
        "product_id" })) //matches database unique constraints

public class WarehouseInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_inventory_id", nullable = false)
    private Integer warehouseInventoryId;

    @Column(name = "warehouse_id", nullable = false)
    private Integer warehouseId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "storage_location", nullable = false)
    private String storageLocation;

    public WarehouseInventory() {
    }

    public WarehouseInventory(int warehouseId, int productId, int quantity, String storageLocation) {
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity;
        this.storageLocation = storageLocation;
    }

    public Integer getWarehouseInventoryId() {
        return warehouseInventoryId;
    }

    public void setWarehouseInventoryId(Integer warehouseInventoryId) {
        this.warehouseInventoryId = warehouseInventoryId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((warehouseInventoryId == null) ? 0 : warehouseInventoryId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WarehouseInventory other = (WarehouseInventory) obj;
        if (warehouseInventoryId == null) {
            if (other.warehouseInventoryId != null)
                return false;
        } else if (!warehouseInventoryId.equals(other.warehouseInventoryId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "WarehouseInventory [warehouseInventoryId=" + warehouseInventoryId + ", warehouseId=" + warehouseId
                + ", productId=" + productId + ", quantity=" + quantity + ", storageLocation=" + storageLocation + "]";
    }

}

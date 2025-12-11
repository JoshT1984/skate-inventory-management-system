package com.skillstorm.skate_inventory_mgmt_system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "WAREHOUSE_INVENTORIES", uniqueConstraints = @UniqueConstraint(columnNames = { "warehouse_id",
        "product_id" }))
public class WarehouseInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_inventory_id", nullable = false)
    private Integer warehouseInventoryId;

    @NotNull
    @Column(name = "warehouse_id", nullable = false)
    private Integer warehouseId;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

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

    public WarehouseInventory(Integer warehouseId,
            Integer productId,
            Integer quantity,
            String storageLocation) {
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
        return "WarehouseInventory [warehouseInventoryId=" + warehouseInventoryId
                + ", warehouseId=" + warehouseId
                + ", productId=" + productId
                + ", quantity=" + quantity
                + ", storageLocation=" + storageLocation + "]";
    }
}

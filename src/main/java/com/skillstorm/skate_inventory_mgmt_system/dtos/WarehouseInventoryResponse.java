package com.skillstorm.skate_inventory_mgmt_system.dtos;

public class WarehouseInventoryResponse {

    public Integer warehouseInventoryId;
    public Integer warehouseId;
    public Integer productId;
    public Integer quantity;
    public String storageLocation;

    public WarehouseInventoryResponse() {
    }

    public WarehouseInventoryResponse(Integer warehouseInventoryId,
            Integer warehouseId,
            Integer productId,
            Integer quantity,
            String storageLocation) {
        this.warehouseInventoryId = warehouseInventoryId;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity;
        this.storageLocation = storageLocation;
    }
}

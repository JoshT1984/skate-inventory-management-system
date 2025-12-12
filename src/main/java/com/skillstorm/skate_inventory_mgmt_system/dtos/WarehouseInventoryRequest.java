package com.skillstorm.skate_inventory_mgmt_system.dtos;

public class WarehouseInventoryRequest {

    public Integer warehouseId;
    public Integer productId;
    public Integer quantity;
    public String storageLocation;

    public WarehouseInventoryRequest() {
    }

    public WarehouseInventoryRequest(Integer warehouseId,
            Integer productId,
            Integer quantity,
            String storageLocation) {
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity;
        this.storageLocation = storageLocation;
    }
}

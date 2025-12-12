package com.skillstorm.skate_inventory_mgmt_system.dtos;

public class WarehouseInventoryTransferRequest {
    public Integer productId;
    public Integer sourceWarehouseId;
    public Integer destinationWarehouseId;
    public Integer quantity;

    public WarehouseInventoryTransferRequest() {
    }

    public WarehouseInventoryTransferRequest(Integer productId,
            Integer sourceWarehouseId,
            Integer destinationWarehouseId,
            Integer quantity) {
        this.productId = productId;
        this.sourceWarehouseId = sourceWarehouseId;
        this.destinationWarehouseId = destinationWarehouseId;
        this.quantity = quantity;
    }
}

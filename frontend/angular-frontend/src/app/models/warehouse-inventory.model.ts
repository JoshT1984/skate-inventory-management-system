export class WarehouseInventory {
  warehouseInventoryId: number;
  warehouseId: number;
  productId: number;
  quantity: number;
  storageLocation: string;

  constructor(
    warehouseInventoryId: number,
    warehouseId: number,
    productId: number,
    quantity: number,
    storageLocation: string
  ) {
    this.warehouseInventoryId = warehouseInventoryId;
    this.warehouseId = warehouseId;
    this.productId = productId;
    this.quantity = quantity;
    this.storageLocation = storageLocation;
  }
}

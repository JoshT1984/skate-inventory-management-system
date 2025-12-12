// src/app/models/warehouse.model.ts
export class Warehouse {
  warehouseId: number;
  name: string;
  location: string;
  address: string;
  maxCapacity: number;
  currentCapacity: number;
  code: string;
  createdAt: Date;
  updatedAt: Date;

  constructor(
    warehouseId: number,
    name: string,
    location: string,
    address: string,
    maxCapacity: number,
    currentCapacity: number,
    code: string,
    createdAt: Date,
    updatedAt: Date
  ) {
    this.warehouseId = warehouseId;
    this.name = name;
    this.location = location;
    this.address = address;
    this.maxCapacity = maxCapacity;
    this.currentCapacity = currentCapacity;
    this.code = code;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}

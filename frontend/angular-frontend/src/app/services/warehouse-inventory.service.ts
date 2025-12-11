// src/app/services/warehouse-inventory.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface WarehouseInventoryDto {
  warehouseInventoryId: number;
  warehouseId: number;
  productId: number;
  quantity: number;
  storageLocation: string;
}

@Injectable({
  providedIn: 'root',
})
export class WarehouseInventoryService {
  private baseUrl = 'http://localhost:8080/warehouse-inventory';

  constructor(private http: HttpClient) {}

  getAllWarehouseInventory(): Observable<WarehouseInventoryDto[]> {
    return this.http.get<WarehouseInventoryDto[]>(this.baseUrl);
  }
}

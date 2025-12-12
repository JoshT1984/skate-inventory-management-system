// src/app/services/warehouse-inventory.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WarehouseInventory } from '../models/warehouse-inventory.model';

@Injectable({
  providedIn: 'root',
})
export class WarehouseInventoryService {
  private baseURL = 'http://localhost:8080/warehouse-inventory';

  constructor(private http: HttpClient) {}

  // READ ALL
  getAllInventory(): Observable<WarehouseInventory[]> {
    return this.http.get<WarehouseInventory[]>(this.baseURL);
  }

  // CREATE
  createInventory(
    payload: Omit<WarehouseInventory, 'warehouseInventoryId'>
  ): Observable<WarehouseInventory> {
    return this.http.post<WarehouseInventory>(this.baseURL, payload);
  }

  //CREATE => TRANSFER INVENTORY
  transferInventory(payload: {
    productId: number;
    sourceWarehouseId: number;
    destinationWarehouseId: number;
    quantity: number;
  }): Observable<void> {
    return this.http.post<void>(`${this.baseURL}/transfer`, payload);
  }

  // UPDATE (PATCH)
  updateInventory(
    id: number,
    payload: Partial<Omit<WarehouseInventory, 'warehouseInventoryId'>>
  ): Observable<WarehouseInventory> {
    return this.http.patch<WarehouseInventory>(`${this.baseURL}/${id}`, payload);
  }

  // DELETE
  deleteInventory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseURL}/${id}`);
  }

  
}

// src/app/services/warehouse.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Warehouse } from '../models/warehouse.model';

@Injectable({
  providedIn: 'root',
})
export class WarehouseService {
  private baseUrl = 'http://localhost:8080/warehouses';

  constructor(private http: HttpClient) {}

  // READ ALL
  getAllWarehouses(): Observable<Warehouse[]> {
    return this.http.get<Warehouse[]>(this.baseUrl);
  }

  // CREATE (no warehouseId in payload)
  createWarehouse(warehouse: any): Observable<Warehouse> {
    return this.http.post<Warehouse>(this.baseUrl, warehouse);
  }

  // UPDATE (PATCH)
  updateWarehouse(warehouse: Warehouse): Observable<Warehouse> {
    return this.http.patch<Warehouse>(`${this.baseUrl}/${warehouse.warehouseId}`, warehouse);
  }

  // DELETE
  deleteWarehouse(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

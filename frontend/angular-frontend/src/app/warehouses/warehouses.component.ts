import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Warehouse } from '../models/warehouse.model';
import { WarehouseService } from '../services/warehouse.service';

@Component({
  selector: 'app-warehouses',
  imports: [CommonModule],
  templateUrl: './warehouses.component.html',
  styleUrl: './warehouses.component.css',
})
export class Warehouses {
  constructor(private warehouseService: WarehouseService) {}

  mockWarehouses: Warehouse[] = [
    new Warehouse(
      1,
      'Warehouse One',
      'Austin',
      '100 Main St',
      2000,
      1200,
      'AUS01',
      new Date(),
      new Date()
    ),
    new Warehouse(
      2,
      'Warehouse Two',
      'Dallas',
      '200 Oak St',
      3000,
      2800,
      'DAL02',
      new Date(),
      new Date()
    ),
    new Warehouse(
      3,
      'Warehouse Three',
      'Houston',
      '300 Pine St',
      1500,
      600,
      'HOU03',
      new Date(),
      new Date()
    ),
  ];

  // the type after <method name>: ist the return type of this method
  addMockWarehouse(): void {
    this.mockWarehouses.push(
      new Warehouse(
        4,
        'Warehouse Four',
        'San Antonio',
        '100 Main St',
        5000,
        1000,
        'SAN04',
        new Date(),
        new Date()
      )
    );
  }
}

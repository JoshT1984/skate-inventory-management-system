import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { WarehouseInventory } from '../models/warehouse-inventory.model';

@Component({
  selector: 'app-warehouse-inventory',
  imports: [CommonModule],
  templateUrl: './warehouse-inventory.component.html',
  styleUrl: './warehouse-inventory.component.css',
})
export class WarehouseInventoryComponent {
  mockWarehouseInventory: WarehouseInventory[] = [
    new WarehouseInventory(1, 1, 6, 120, 'A1-Top-Shelf'),
    new WarehouseInventory(2, 1, 7, 40, 'A2-Bin-3'),
    new WarehouseInventory(3, 2, 8, 75, 'B1-Row-2'),
    new WarehouseInventory(4, 3, 9, 15, 'C3-Locker-7'),
    new WarehouseInventory(5, 2, 10, 200, 'B4-Top-Shelf'),
    new WarehouseInventory(6, 3, 11, 50, 'C1-Bin-1'),
    new WarehouseInventory(7, 1, 12, 30, 'A4-Row-1'),
  ];

  addMockWarehouseInventory(): void {
    this.mockWarehouseInventory.push(
      new WarehouseInventory(8, 2, 13, 60, 'B5-Bin-4')
    );
  }
}

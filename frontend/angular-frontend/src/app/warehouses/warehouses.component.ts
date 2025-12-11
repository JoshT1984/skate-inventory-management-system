import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Warehouse } from '../models/warehouse.model';
import { WarehouseService } from '../services/warehouse.service';

@Component({
  selector: 'app-warehouses',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './warehouses.component.html',
  styleUrl: './warehouses.component.css',
})
export class Warehouses implements OnInit {
  warehouses: Warehouse[] = [];

  constructor(
    private warehouseService: WarehouseService,
    private cdr: ChangeDetectorRef // 👈 add this
  ) {}

  ngOnInit(): void {
    this.loadWarehouses();
  }

  loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (warehouses) => {
        this.warehouses = warehouses;
        this.cdr.detectChanges(); // 👈 force view update
      },
      error: (err) => {
        console.error('Failed to read warehouses', err);
        this.cdr.detectChanges();
      },
    });
  }
}

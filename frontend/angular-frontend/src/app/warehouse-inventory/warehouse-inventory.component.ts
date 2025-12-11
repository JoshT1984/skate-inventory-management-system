import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { WarehouseInventoryService } from '../services/warehouse-inventory.service';

@Component({
  selector: 'app-warehouse-inventory',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './warehouse-inventory.component.html',
  styleUrl: './warehouse-inventory.component.css',
})
export class WarehouseInventoryComponent implements OnInit {
  inventory: any[] = [];
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private warehouseInventoryService: WarehouseInventoryService,
    private cdr: ChangeDetectorRef // 👈 inject ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    console.log('WarehouseInventoryComponent ngOnInit called');
    this.loadInventory();
  }

  loadInventory(): void {
    console.log('loadInventory() started - REAL HTTP');

    this.loading = true;
    this.errorMessage = null;
    this.inventory = [];

    this.warehouseInventoryService.getAllWarehouseInventory().subscribe({
      next: (items) => {
        console.log('HTTP next fired, items:', items);

        this.inventory = Array.isArray(items) ? items : [];
        this.loading = false;

        // 👇 Force Angular to re-check the view right now
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('HTTP error fired:', err);
        this.errorMessage = 'Failed to load warehouse inventory.';
        this.loading = false;

        this.cdr.detectChanges(); // also update after errors
      },
    });
  }
}

import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Warehouse } from '../models/warehouse.model';
import { WarehouseService } from '../services/warehouse.service';

@Component({
  selector: 'app-warehouses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './warehouses.component.html',
  styleUrls: ['./warehouses.component.css'],
})
export class Warehouses implements OnInit {
  warehouses: Warehouse[] = [];

  // form state for creating a new warehouse (no id, no timestamps)
  newWarehouseForm = {
    name: '',
    location: '',
    address: '',
    maxCapacity: 0,
    currentCapacity: 0,
    code: '',
  };

  editingWarehouse: Warehouse | null = null;

  apiError: string | null = null;
  apiSuccess: string | null = null;
  isLoading = false;

  constructor(private warehouseService: WarehouseService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadWarehouses();
  }

  // READ ALL
  loadWarehouses(): void {
    this.isLoading = true;
    this.clearMessages();

    this.warehouseService.getAllWarehouses().subscribe({
      next: (warehouses) => {
        this.warehouses = warehouses.map(
          (w) =>
            new Warehouse(
              w.warehouseId,
              w.name,
              w.location,
              w.address,
              w.maxCapacity,
              w.currentCapacity,
              w.code,
              new Date(w.createdAt),
              new Date(w.updatedAt)
            )
        );
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to read warehouses', err);
        this.apiError = 'Failed to load warehouses. Please try again.';
        this.isLoading = false;
        this.cdr.detectChanges();
      },
    });
  }

  // CREATE (no warehouseId in payload)
  createWarehouse(form: NgForm): void {
    this.clearMessages();

    if (!this.newWarehouseForm.name || !this.newWarehouseForm.location) {
      this.apiError = 'Name and location are required to create a warehouse.';
      return;
    }

    this.isLoading = true;

    const payload = {
      name: this.newWarehouseForm.name,
      location: this.newWarehouseForm.location,
      address: this.newWarehouseForm.address,
      maxCapacity: this.newWarehouseForm.maxCapacity,
      currentCapacity: this.newWarehouseForm.currentCapacity,
      code: this.newWarehouseForm.code,
    };

    this.warehouseService.createWarehouse(payload).subscribe({
      next: (created) => {
        const w = new Warehouse(
          created.warehouseId,
          created.name,
          created.location,
          created.address,
          created.maxCapacity,
          created.currentCapacity,
          created.code,
          new Date(created.createdAt),
          new Date(created.updatedAt)
        );

        this.warehouses.push(w);

        // 🔥 Reset the form AND reset the model values
        form.resetForm({
          name: '',
          location: '',
          address: '',
          maxCapacity: null,
          currentCapacity: null,
          code: '',
        });

        this.showSuccess('Warehouse created successfully.');
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to create warehouse', err);
        this.isLoading = false;

        if (err.status === 409) {
          this.apiError = 'A warehouse with that code already exists.';
        } else if (err.status === 400) {
          this.apiError = err.error?.message ?? 'Invalid warehouse data. Please check your inputs.';
        } else {
          this.apiError = 'An unexpected error occurred while creating the warehouse.';
        }

        this.cdr.detectChanges();
      },
    });
  }

  // ENTER EDIT MODE
  startEdit(warehouse: Warehouse): void {
    this.clearMessages();

    this.editingWarehouse = new Warehouse(
      warehouse.warehouseId,
      warehouse.name,
      warehouse.location,
      warehouse.address,
      warehouse.maxCapacity,
      warehouse.currentCapacity,
      warehouse.code,
      warehouse.createdAt,
      warehouse.updatedAt
    );
    this.cdr.detectChanges();
  }

  // CANCEL EDIT
  cancelEdit(): void {
    this.editingWarehouse = null;
    this.clearMessages();
    this.cdr.detectChanges();
  }

  // UPDATE (PATCH)
  updateWarehouse(): void {
    if (!this.editingWarehouse) {
      this.apiError = 'No warehouse selected for update.';
      return;
    }

    this.clearMessages();
    this.isLoading = true;

    this.warehouseService.updateWarehouse(this.editingWarehouse).subscribe({
      next: (updated) => {
        this.warehouses = this.warehouses.map((w) =>
          w.warehouseId === updated.warehouseId
            ? new Warehouse(
                updated.warehouseId,
                updated.name,
                updated.location,
                updated.address,
                updated.maxCapacity,
                updated.currentCapacity,
                updated.code,
                new Date(updated.createdAt),
                new Date(updated.updatedAt)
              )
            : w
        );
        this.editingWarehouse = null;
        this.isLoading = false;
        this.showSuccess('Warehouse updated successfully.');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to update warehouse', err);
        this.isLoading = false;
        this.apiError = 'An unexpected error occurred while updating the warehouse.';
        this.cdr.detectChanges();
      },
    });
  }

  // DELETE
  deleteWarehouse(id: number): void {
    if (!confirm('Are you sure you want to delete this warehouse?')) return;

    this.isLoading = true;

    this.warehouseService.deleteWarehouse(id).subscribe({
      next: () => {
        this.warehouses = this.warehouses.filter((w) => w.warehouseId !== id);
        this.isLoading = false;
        this.showSuccess('Warehouse deleted successfully.');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to delete warehouse', err);
        this.isLoading = false;
        this.apiError = 'An unexpected error occurred while deleting the warehouse.';
        this.cdr.detectChanges();
      },
    });
  }

  private clearMessages(): void {
    this.apiError = null;
    this.apiSuccess = null;
  }

  private scrollToTop(): void {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }

  private showSuccess(message: string): void {
    this.apiSuccess = message;
    this.scrollToTop();

    // Optional: auto-hide after 3 seconds
    setTimeout(() => {
      if (this.apiSuccess === message) {
        this.apiSuccess = null;
      }
    }, 3000);
  }
}

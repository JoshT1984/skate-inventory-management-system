import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Warehouse } from '../models/warehouse.model';
import { WarehouseService } from '../services/warehouse.service';
import { AuthService } from '../services/auth.service';

type WarehouseSortField =
  | 'name'
  | 'location'
  | 'address'
  | 'maxCapacity'
  | 'currentCapacity'
  | 'code';
type SortDirection = 'asc' | 'desc';

@Component({
  selector: 'app-warehouses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './warehouses.component.html',
  styleUrls: ['./warehouses.component.css'],
})
export class Warehouses implements OnInit {
  warehouses: Warehouse[] = [];

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

  searchTerm = '';
  sortField: WarehouseSortField = 'name';
  sortDirection: SortDirection = 'asc';

  constructor(
    private warehouseService: WarehouseService,
    private cdr: ChangeDetectorRef,
    public authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.loadWarehouses();
  }

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
              new Date(w.updatedAt),
            ),
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

  createWarehouse(form: NgForm): void {
    this.clearMessages();

    if (!this.newWarehouseForm.name || !this.newWarehouseForm.location) {
      this.apiError = 'Name and location are required to create a warehouse.';
      return;
    }

    this.isLoading = true;

    const payload = {
      name: this.newWarehouseForm.name.trim(),
      location: this.newWarehouseForm.location.trim(),
      address: this.newWarehouseForm.address.trim(),
      maxCapacity: this.newWarehouseForm.maxCapacity,
      currentCapacity: this.newWarehouseForm.currentCapacity,
      code: this.newWarehouseForm.code.trim(),
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
          new Date(created.updatedAt),
        );

        this.warehouses = [w, ...this.warehouses];

        form.resetForm({
          name: '',
          location: '',
          address: '',
          maxCapacity: 0,
          currentCapacity: 0,
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
      warehouse.updatedAt,
    );

    this.scrollToTop();
    this.cdr.detectChanges();
  }

  cancelEdit(): void {
    this.editingWarehouse = null;
    this.clearMessages();
    this.cdr.detectChanges();
  }

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
                new Date(updated.updatedAt),
              )
            : w,
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

  get filteredWarehouses(): Warehouse[] {
    const term = this.searchTerm.trim().toLowerCase();

    let filtered = this.warehouses.filter((w) => {
      if (!term) return true;

      return (
        w.name.toLowerCase().includes(term) ||
        w.location.toLowerCase().includes(term) ||
        w.address.toLowerCase().includes(term) ||
        w.code.toLowerCase().includes(term)
      );
    });

    filtered = [...filtered].sort((a, b) => {
      const aValue = a[this.sortField];
      const bValue = b[this.sortField];

      if (typeof aValue === 'number' && typeof bValue === 'number') {
        return this.sortDirection === 'asc' ? aValue - bValue : bValue - aValue;
      }

      const compare = String(aValue ?? '')
        .toLowerCase()
        .localeCompare(String(bValue ?? '').toLowerCase());
      return this.sortDirection === 'asc' ? compare : -compare;
    });

    return filtered;
  }

  setSort(field: WarehouseSortField): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
  }

  capacityBadgeClass(warehouse: Warehouse): string {
    const ratio = warehouse.maxCapacity > 0 ? warehouse.currentCapacity / warehouse.maxCapacity : 0;

    if (ratio >= 1) return 'badge badge-danger'; // Full
    if (ratio >= 0.85) return 'badge badge-warning'; // Almost full
    if (ratio === 0) return 'badge badge-warning'; // Empty
    return 'badge badge-success'; // Available
  }

  capacityLabel(warehouse: Warehouse): string {
    const ratio = warehouse.maxCapacity > 0 ? warehouse.currentCapacity / warehouse.maxCapacity : 0;

    if (ratio >= 1) return 'Full';
    if (ratio >= 0.85) return 'Almost Full';
    if (ratio === 0) return 'Empty';
    return 'Available';
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

    setTimeout(() => {
      if (this.apiSuccess === message) {
        this.apiSuccess = null;
        this.cdr.detectChanges();
      }
    }, 3000);
  }

  canManage(): boolean {
    return this.authService.hasAnyRole(['ADMIN', 'MANAGER']);
  }
}

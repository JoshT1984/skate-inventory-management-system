import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Product } from '../models/product.model';
import { WarehouseInventory } from '../models/warehouse-inventory.model';
import { Warehouse } from '../models/warehouse.model';
import { ProductService } from '../services/product.service';
import { WarehouseInventoryService } from '../services/warehouse-inventory.service';
import { WarehouseService } from '../services/warehouse.service';

@Component({
  selector: 'app-warehouse-inventory',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './warehouse-inventory.component.html',
  styleUrls: ['./warehouse-inventory.component.css'],
})
export class WarehouseInventoryComponent implements OnInit {
  inventory: WarehouseInventory[] = [];

  // options for dropdowns
  warehouses: Warehouse[] = [];
  products: Product[] = [];

  // Form model for new inventory row
  newInventoryForm: {
    warehouseId: number | null;
    productId: number | null;
    quantity: number | null;
    storageLocation: string;
  } = {
    warehouseId: null,
    productId: null,
    quantity: null,
    storageLocation: '',
  };

  // Row currently being edited
  editingItem: WarehouseInventory | null = null;

  // transfer mode
  transferItem: WarehouseInventory | null = null;
  transferForm = {
    destinationWarehouseId: null as number | null,
    quantity: null as number | null,
  };

  isLoading = false;
  apiError: string | null = null;
  apiSuccess: string | null = null;

  @ViewChild('createInventoryForm') createInventoryForm!: NgForm;

  constructor(
    private inventoryService: WarehouseInventoryService,
    private cdr: ChangeDetectorRef,
    private warehouseService: WarehouseService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.loadInventory();
    this.loadWarehouses();
    this.loadProducts();
  }

  // Helper: clear messages
  private clearMessages(): void {
    this.apiError = null;
    this.apiSuccess = null;
  }

  // load warehouses
  private loadWarehouses(): void {
    this.warehouseService.getAllWarehouses().subscribe({
      next: (data) => {
        this.warehouses = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load warehouses', err);
      },
    });
  }

  // load products
  private loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load products', err);
      },
    });
  }

  // READ ALL
  loadInventory(clearMessages: boolean = true): void {
    this.isLoading = true;
    if (clearMessages) {
      this.clearMessages();
    }

    this.inventoryService.getAllInventory().subscribe({
      next: (items) => {
        this.inventory = items;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.apiError = 'Failed to load inventory. Please try again.';
        console.error('Failed to load inventory', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
    });
  }

  // CREATE
  createInventory(): void {
    this.clearMessages();

    if (
      this.newInventoryForm.warehouseId == null ||
      this.newInventoryForm.productId == null ||
      this.newInventoryForm.quantity == null ||
      this.newInventoryForm.storageLocation.trim().length === 0
    ) {
      this.apiError = 'All fields are required to create an inventory item.';
      return;
    }

    this.isLoading = true;

    const payload = {
      warehouseId: this.newInventoryForm.warehouseId,
      productId: this.newInventoryForm.productId,
      quantity: this.newInventoryForm.quantity,
      storageLocation: this.newInventoryForm.storageLocation.trim(),
    };

    this.inventoryService.createInventory(payload).subscribe({
      next: (created) => {
        this.inventory.push(created);

        this.newInventoryForm = {
          warehouseId: null,
          productId: null,
          quantity: null,
          storageLocation: '',
        };

        this.showSuccess('Inventory item created successfully.');
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;

        if (err.status === 409) {
          this.apiError = 'This warehouse already has an inventory entry for that product.';
        } else if (err.status === 400) {
          this.apiError = err.error?.message ?? 'Invalid inventory data. Please check your input.';
        } else if (err.status === 404) {
          this.apiError = 'Warehouse or Product not found. Please check your selections.';
        } else {
          this.apiError = 'An unexpected error occurred while creating inventory.';
        }

        console.error('Failed to create inventory item', err);
        this.cdr.detectChanges();
      },
    });
  }

  // START TRANSFER
  startTransfer(item: WarehouseInventory): void {
    this.clearMessages();

    // reset validation on the Add form so fields do not flash red
    if (this.createInventoryForm) {
      this.createInventoryForm.form.markAsPristine();
      this.createInventoryForm.form.markAsUntouched();
    }

    this.transferItem = item;
    this.transferForm = {
      destinationWarehouseId: null,
      quantity: null,
    };

    this.cdr.detectChanges();
    this.scrollToTop();
  }

  cancelTransfer(): void {
    this.transferItem = null;
    this.transferForm = {
      destinationWarehouseId: null,
      quantity: null,
    };
    this.cdr.detectChanges();
  }

  submitTransfer(): void {
    if (!this.transferItem) {
      this.apiError = 'No inventory item selected for transfer.';
      return;
    }

    if (
      !this.transferForm.destinationWarehouseId ||
      !this.transferForm.quantity ||
      this.transferForm.quantity <= 0
    ) {
      this.apiError = 'Destination warehouse and a positive quantity are required.';
      return;
    }

    this.isLoading = true;
    this.clearMessages();

    const payload = {
      productId: this.transferItem.productId,
      sourceWarehouseId: this.transferItem.warehouseId,
      destinationWarehouseId: this.transferForm.destinationWarehouseId,
      quantity: this.transferForm.quantity,
    };

    this.inventoryService.transferInventory(payload).subscribe({
      next: () => {
        this.isLoading = false;

        // success message + scroll
        this.showSuccess('Inventory transferred successfully.');

        // reset transfer state
        this.transferItem = null;
        this.transferForm = {
          destinationWarehouseId: null,
          quantity: null,
        };

        // reload inventory, but do NOT clear the success message
        this.loadInventory(false);

        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Failed to transfer inventory', err);

        if (err.status === 400) {
          this.apiError =
            err.error?.message ?? 'Invalid transfer request. Please check quantity and warehouses.';
        } else if (err.status === 404) {
          this.apiError = 'Source/destination warehouse or product not found for transfer.';
        } else {
          this.apiError = 'An unexpected error occurred while transferring inventory.';
        }

        this.cdr.detectChanges();
      },
    });
  }

  // START EDIT
  startEdit(item: WarehouseInventory): void {
    this.clearMessages();

    this.editingItem = {
      warehouseInventoryId: item.warehouseInventoryId,
      warehouseId: item.warehouseId,
      productId: item.productId,
      quantity: item.quantity,
      storageLocation: item.storageLocation,
    };

    this.cdr.detectChanges();
  }

  // CANCEL EDIT
  cancelEdit(): void {
    this.editingItem = null;
    this.clearMessages();
    this.cdr.detectChanges();
  }

  // UPDATE
  updateInventory(): void {
    if (!this.editingItem || this.editingItem.warehouseInventoryId == null) {
      return;
    }

    this.clearMessages();

    if (
      this.editingItem.warehouseId == null ||
      this.editingItem.productId == null ||
      this.editingItem.quantity == null ||
      this.editingItem.storageLocation.trim().length === 0
    ) {
      this.apiError = 'All fields are required to update an inventory item.';
      return;
    }

    const id = this.editingItem.warehouseInventoryId;

    const payload = {
      warehouseId: this.editingItem.warehouseId,
      productId: this.editingItem.productId,
      quantity: this.editingItem.quantity,
      storageLocation: this.editingItem.storageLocation.trim(),
    };

    this.isLoading = true;

    this.inventoryService.updateInventory(id, payload).subscribe({
      next: (updated) => {
        this.inventory = this.inventory.map((it) =>
          it.warehouseInventoryId === updated.warehouseInventoryId ? updated : it
        );

        this.editingItem = null;
        this.showSuccess('Inventory item updated successfully.');
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;

        if (err.status === 409) {
          this.apiError = 'This warehouse already has an inventory entry for that product.';
        } else if (err.status === 400) {
          this.apiError = err.error?.message ?? 'Invalid inventory data. Please check your input.';
        } else if (err.status === 404) {
          this.apiError = 'Inventory item, warehouse, or product not found.';
        } else {
          this.apiError = 'An unexpected error occurred while updating inventory.';
        }

        console.error('Failed to update inventory item', err);
        this.cdr.detectChanges();
      },
    });
  }

  // DELETE
  deleteInventory(id: number): void {
    if (!confirm('Are you sure you want to delete this inventory item?')) return;

    this.clearMessages();
    this.isLoading = true;

    this.inventoryService.deleteInventory(id).subscribe({
      next: () => {
        this.inventory = this.inventory.filter((it) => it.warehouseInventoryId !== id);
        this.showSuccess('Inventory item deleted successfully.');
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.apiError = 'An error occurred while deleting the inventory item.';
        console.error('Failed to delete inventory item', err);
        this.cdr.detectChanges();
      },
    });
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
}

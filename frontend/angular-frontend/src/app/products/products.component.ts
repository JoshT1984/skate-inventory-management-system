import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Product } from '../models/product.model';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css'],
})
export class Products implements OnInit {
  products: Product[] = [];

  // Simple form object for creating a new product (no id here)
  newProductForm = {
    name: '',
    sku: '',
    category: '',
    brand: '',
    description: '',
  };

  // for edit mode
  editingProduct: Product | null = null;

  apiError: string | null = null;
  apiSuccess: string | null = null;
  isLoading = false;
  searchTerm: string = '';

  constructor(private productService: ProductService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.getAllProducts();
  }

  // READ ALL
  getAllProducts(): void {
    this.isLoading = true;
    this.clearMessages();

    this.productService.getAllProducts().subscribe({
      next: (body) => {
        this.products = body.map(
          (p) => new Product(p.productId, p.name, p.sku, p.category, p.brand, p.description)
        );
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to read all products', err);
        this.apiError = 'Failed to load products. Please try again.';
        this.isLoading = false;
        this.cdr.detectChanges();
      },
    });
  }

  // CREATE (no productId in payload)
  createProduct(): void {
    this.clearMessages();

    if (!this.newProductForm.name || !this.newProductForm.sku) {
      this.apiError = 'Name and SKU are required to create a product.';
      return;
    }

    this.isLoading = true;

    const payload = {
      name: this.newProductForm.name,
      sku: this.newProductForm.sku,
      category: this.newProductForm.category,
      brand: this.newProductForm.brand,
      description: this.newProductForm.description,
    };

    this.productService.createProduct(payload as any).subscribe({
      next: (created) => {
        const newProd = new Product(
          created.productId,
          created.name,
          created.sku,
          created.category,
          created.brand,
          created.description
        );
        this.products.push(newProd);

        // reset the form
        this.newProductForm = {
          name: '',
          sku: '',
          category: '',
          brand: '',
          description: '',
        };

        this.isLoading = false;
        this.showSuccess('Product created successfully.');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to create product', err);
        this.isLoading = false;

        if (err.status === 409) {
          this.apiError = 'A product with that SKU already exists.';
        } else if (err.status === 400) {
          this.apiError = err.error?.message ?? 'Invalid product data. Please check your inputs.';
        } else {
          this.apiError = 'An unexpected error occurred while creating the product.';
        }

        this.cdr.detectChanges();
      },
    });
  }

  // ENTER EDIT MODE
  startEdit(product: Product): void {
    this.clearMessages();

    // shallow copy so you don't mutate the table row directly
    this.editingProduct = new Product(
      product.productId,
      product.name,
      product.sku,
      product.category,
      product.brand,
      product.description
    );
    this.cdr.detectChanges();
  }

  // CANCEL EDIT
  cancelEdit(): void {
    this.editingProduct = null;
    this.clearMessages();
    this.cdr.detectChanges();
  }

  // UPDATE (PATCH)
  updateProduct(): void {
    if (!this.editingProduct) {
      this.apiError = 'No product selected for update.';
      return;
    }

    this.clearMessages();
    this.isLoading = true;

    this.productService.updateProduct(this.editingProduct).subscribe({
      next: (updated) => {
        this.products = this.products.map((p) =>
          p.productId === updated.productId
            ? new Product(
                updated.productId,
                updated.name,
                updated.sku,
                updated.category,
                updated.brand,
                updated.description
              )
            : p
        );

        this.editingProduct = null;
        this.isLoading = false;
        this.showSuccess('Product updated successfully.');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to update product', err);
        this.isLoading = false;
        this.apiError = 'An unexpected error occurred while updating the product.';
        this.cdr.detectChanges();
      },
    });
  }

  // DELETE
  deleteProduct(id: number): void {
    if (!confirm('Are you sure you want to delete this product?')) return;

    this.isLoading = true;
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.products = this.products.filter((p) => p.productId !== id);
        this.isLoading = false;
        this.showSuccess('Product deleted successfully.');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to delete product', err);
        this.isLoading = false;
        this.apiError = 'An unexpected error occurred while deleting the product.';
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

    // Optional auto-hide
    setTimeout(() => {
      if (this.apiSuccess === message) {
        this.apiSuccess = null;
        this.cdr.detectChanges?.();
      }
    }, 3000);
  }

  //Adds Product Search Functionality
  get filteredProducts(): Product[] {
    const term = this.searchTerm.toLowerCase();
    return this.products.filter(
      (p) =>
        p.name.toLowerCase().includes(term) ||
        p.sku.toLowerCase().includes(term) ||
        (p.category?.toLowerCase().includes(term) ?? false) ||
        (p.brand?.toLowerCase().includes(term) ?? false)
    );
  }
}

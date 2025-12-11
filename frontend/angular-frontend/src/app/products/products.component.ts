import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Product } from '../models/product.model';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css',
})
export class Products implements OnInit {
  products: Product[] = [];

  constructor(private productService: ProductService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.getAllProducts();
  }

  // READ
  getAllProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (body) => {
        this.products = body.map(
          (p) => new Product(p.productId, p.name, p.sku, p.category, p.brand, p.description)
        );
        this.cdr.detectChanges(); // ensure view updates after async call
      },
      error: (err) => {
        console.error('Failed to read all products', err);
        this.cdr.detectChanges();
      },
    });
  }

  // DELETE
  deleteProduct(id: number): void {
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        console.log(`Product ${id} deleted successfully`);
        // Update local array so the table re-renders
        this.products = this.products.filter((p) => p.productId !== id);
        this.cdr.detectChanges(); // ensure view updates after delete
      },
      error: (err) => {
        console.error('Failed to delete product', err);
        this.cdr.detectChanges();
      },
    });
  }
}

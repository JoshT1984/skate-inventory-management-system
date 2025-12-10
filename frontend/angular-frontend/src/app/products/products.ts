import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Product } from '../models/product'; //importing the Product class

@Component({
  selector: 'app-products',
  imports: [CommonModule], //Older syntax but still used for older functionality
  templateUrl: './products.html',
  styleUrl: './products.css',
})
export class Products {
  // TypeScript, we ideally give our variables a type
  // once we do, that variable can only hold values of that type
  // you must import the external Product class to make this work
  // mockProduct: Product = new Product(6, 'Joshua', 'Thompson', '1910-06-12', 100000.0, 3);

  mockProducts: Product[] = [
    new Product(6, 'ipad', 'sku-1567', 'electronics', 'apple', 'mobile-tablet 64GB ssd'),
    new Product(
      7,
      'laptop',
      'sku-1667',
      'electronics',
      'Windows',
      'mobile-laptop 16gb Ram, 500gb ssd'
    ),
    new Product(8, 'mobile-phone', 'sku-1764', 'electronics', 'Samsung', 'android galaxy S25U'),
    new Product(
      9,
      'toy-computer',
      'sku-1765',
      'electronics',
      'Leap Frog',
      'kid-friendly computer toy'
    ),
    new Product(10, 'mobile-phone', 'sku-1766', 'electronics', 'apple', 'iphone 15 ultra'),
    new Product(11, 'ipad', 'sku-1767', 'electronics', 'apple', 'mobile-tablet 64GB ssd'),
    new Product(12, 'radio', 'sku-1850', 'electronics', 'sony', 'car stereo'),
  ];

  // the type after <method name>: ist the return type of this method
  addMockProduct(): void {
    this.mockProducts.push(
      new Product(13, 'laptop', 'sku-1876', 'electronics', 'linux', 'custom-built laptop')
    );
  }
}

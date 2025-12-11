import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ItemComponent } from '../item/item.component';
import { Item } from '../models/item.model';
import { Product } from '../models/product.model'; //importing the Product class
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-products',
  imports: [CommonModule, ItemComponent], //Older syntax but still used for older functionality
  templateUrl: './products.component.html',
  styleUrl: './products.component.css',
})
export class Products {
  constructor(private productService: ProductService) {}

  // TypeScript, we ideally give our variables a type
  // once we do, that variable can only hold values of that type
  // you must import the external Product class to make this work
  // mockProduct: Product = new Product(6, 'Joshua', 'Thompson', '1910-06-12', 100000.0, 3);

  mockItem: Item[] = [
    new Item(1, 'football', 'medium', 'brown'),
    new Item(2, 'soccer ball', 'medium', 'white and black'),
    new Item(3, 'tennis ball', 'small', 'green'),
    new Item(4, 'baseball', 'small', 'white'),
  ];

  //this method should actually remove the mockItem data when clicked:

  deleteMockItem(index: number, message: string) {
    console.log(message);
    this.mockItem.splice(index, 1);
  }

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

// src/app/services/product.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // Base URL to Postgres via Spring Boot
  baseURL: string = 'http://localhost:8080/products';

  constructor(private http: HttpClient) {}

  // READ ALL
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseURL);
  }

  // CREATE
  createProduct(product: any): Observable<Product> {
    // backend should ignore productId or set it
    return this.http.post<Product>(this.baseURL, product);
  }

  // UPDATE
  updateProduct(product: Product): Observable<Product> {
    return this.http.patch<Product>(`${this.baseURL}/${product.productId}`, product);
  }

  // DELETE
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseURL}/${id}`);
  }
}

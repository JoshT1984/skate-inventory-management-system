import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private http: HttpClient) {}
  //Base URL to PostGres
  baseURL: string = 'http://localhost:8080/products';

  // to get all products
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseURL);
  }

  //DELETE
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseURL}/${id}`);
  }
}

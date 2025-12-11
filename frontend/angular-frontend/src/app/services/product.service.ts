import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private http: HttpClient) {
    this.getAllProducts().subscribe((data) => {
      console.log(data);
    });
  }
  // we can write methods here that make HTTP calls of any type

  // to get all products
  getAllProducts(): Observable<any> {
    //first param = URL for the request
    //second param = what data you want to observe from the response
    return this.http.get('http://localhost:8080/products', {
      observe: 'response',
    });
  }
}

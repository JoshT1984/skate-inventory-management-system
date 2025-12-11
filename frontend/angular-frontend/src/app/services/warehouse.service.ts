import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class WarehouseService {
  constructor(private http: HttpClient) {
    this.getAllWarehouses().subscribe((data) => {
      console.log(data);
    });
  }

  getAllWarehouses(): Observable<any> {
    return this.http.get('http://localhost:8080/warehouses', {
      observe: 'response',
    });
  }
}

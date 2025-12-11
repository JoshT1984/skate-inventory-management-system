import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WarehouseInventoryService {
  constructor(private http: HttpClient) {
    this.getAllWarehouseInventory().subscribe((data) => {
      console.log(data);
    });
  }

  getAllWarehouseInventory(): Observable<any> {
    return this.http.get('localhost://8080/warehouses', {
      observe: 'response',
    });
  }
}

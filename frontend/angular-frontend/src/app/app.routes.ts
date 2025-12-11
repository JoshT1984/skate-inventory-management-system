import { Routes } from '@angular/router';
import { Products } from './products/products.component';
import { WarehouseInventoryComponent } from './warehouse-inventory/warehouse-inventory.component';
import { Warehouses } from './warehouses/warehouses.component';

/**
 *  each item in the route array is a single path
 *  the path property is whatever's after the base URL in the browser
 *  the component property points to the component to load
 */
export const routes: Routes = [
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
  {
    path: 'products',
    component: Products,
  },
  {
    path: 'warehouses',
    component: Warehouses,
  },
  {
    path: 'warehouse-inventory',
    component: WarehouseInventoryComponent,
  },
  // Optional: wildcard to catch unknown routes
  {
    path: '**',
    redirectTo: 'products',
  },
];

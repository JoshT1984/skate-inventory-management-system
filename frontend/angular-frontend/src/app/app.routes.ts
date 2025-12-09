import { Routes } from '@angular/router';
import { Products } from './products/products';
import { Warehouses } from './warehouses/warehouses';

/**
 *  each item in the route array is a single path
 *  the path property is whatever's after the base URL in the browser
 *  the component property points to the component to load
 */
export const routes: Routes = [
  {
    path: 'products',
    component: Products,
  },
  {
    path: 'warehouses',
    component: Warehouses,
  },
];

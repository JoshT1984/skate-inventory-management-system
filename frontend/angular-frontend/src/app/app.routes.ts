import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { LoginComponent } from './login/login.component';
import { Products } from './products/products.component';
import { WarehouseInventoryComponent } from './warehouse-inventory/warehouse-inventory.component';
import { Warehouses } from './warehouses/warehouses.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'products',
    component: Products,
    canActivate: [authGuard],
  },
  {
    path: 'warehouses',
    component: Warehouses,
    canActivate: [authGuard],
  },
  {
    path: 'warehouse-inventory',
    component: WarehouseInventoryComponent,
    canActivate: [authGuard],
  },
  {
    path: 'admin',
    component: Warehouses,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'MANAGER'] },
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];

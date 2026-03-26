import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const roles = (route.data['roles'] as string[]) ?? [];

  if (auth.isAuthenticated() && auth.hasAnyRole(roles)) {
    return true;
  }

  return router.createUrlTree(['/login']);
};

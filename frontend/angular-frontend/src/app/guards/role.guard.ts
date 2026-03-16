import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const roles = (route.data['roles'] as string[]) ?? [];

  return auth.bootstrap().pipe(
    map(() => {
      if (auth.isAuthenticated() && auth.hasAnyRole(roles)) {
        return true;
      }
      return router.createUrlTree(['/products']);
    })
  );
};

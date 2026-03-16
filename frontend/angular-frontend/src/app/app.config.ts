import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { APP_INITIALIZER, ApplicationConfig, inject, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';
import { AuthService } from './services/auth.service';

function bootstrapAuthFactory() {
  const auth = inject(AuthService);
  return () => {
    auth.fetchCsrf().subscribe({
      next: () => auth.bootstrap().subscribe(),
      error: () => auth.bootstrap().subscribe(),
    });
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    {
      provide: APP_INITIALIZER,
      multi: true,
      useFactory: bootstrapAuthFactory,
    },
  ],
};

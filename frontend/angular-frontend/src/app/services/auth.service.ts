import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { catchError, map, of, tap } from 'rxjs';
import { apiConfig } from '../core/api.config';
import { AuthUser } from '../models/auth-user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly currentUserSignal = signal<AuthUser | null>(null);
  private readonly readySignal = signal(false);

  readonly currentUser = computed(() => this.currentUserSignal());
  readonly isReady = computed(() => this.readySignal());
  readonly isAuthenticated = computed(() => !!this.currentUserSignal()?.authenticated);

  bootstrap() {
    return this.http.get<AuthUser>(`${apiConfig.apiBaseUrl}/auth/me`, { withCredentials: true }).pipe(
      tap((user) => {
        this.currentUserSignal.set(user);
        this.readySignal.set(true);
      }),
      catchError(() => {
        this.currentUserSignal.set(null);
        this.readySignal.set(true);
        return of(null);
      })
    );
  }

  fetchCsrf() {
    return this.http.get(`${apiConfig.apiBaseUrl}/csrf`, { withCredentials: true });
  }

  login(): void {
    window.location.href = apiConfig.loginUrl;
  }

  logout() {
    return this.http.post(`${apiConfig.apiBaseUrl}/auth/logout`, {}, { withCredentials: true }).pipe(
      tap(() => this.currentUserSignal.set(null)),
      map(() => true),
      catchError(() => of(false))
    );
  }

  hasRole(role: string): boolean {
    return this.currentUserSignal()?.roles?.includes(`ROLE_${role}`) ?? false;
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some((role) => this.hasRole(role));
  }
}

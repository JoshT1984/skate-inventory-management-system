import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { catchError, map, Observable, of, shareReplay, tap } from 'rxjs';
import { apiConfig } from '../core/api.config';
import { AuthUser } from '../models/auth-user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly currentUserSignal = signal<AuthUser | null>(null);
  private readonly readySignal = signal(false);

  private bootstrapRequest$: Observable<AuthUser | null> | null = null;

  readonly currentUser = computed(() => this.currentUserSignal());
  readonly isReady = computed(() => this.readySignal());
  readonly isAuthenticated = computed(() => !!this.currentUserSignal()?.authenticated);

  bootstrap(force = false): Observable<AuthUser | null> {
    if (this.readySignal() && !force) {
      return of(this.currentUserSignal());
    }

    if (this.bootstrapRequest$ && !force) {
      return this.bootstrapRequest$;
    }

    this.bootstrapRequest$ = this.http
      .get<AuthUser>(`${apiConfig.apiBaseUrl}/auth/me`, { withCredentials: true })
      .pipe(
        tap((user) => {
          this.currentUserSignal.set(user);
          this.readySignal.set(true);
        }),
        catchError(() => {
          this.currentUserSignal.set(null);
          this.readySignal.set(true);
          return of(null);
        }),
        shareReplay(1),
      );

    return this.bootstrapRequest$;
  }

  login(): void {
    window.location.href = apiConfig.loginUrl;
  }

  logout() {
    return this.http.post(`${apiConfig.apiBaseUrl}/logout`, {}, { withCredentials: true }).pipe(
      tap(() => {
        this.currentUserSignal.set(null);
        this.readySignal.set(true);
        this.bootstrapRequest$ = null;
      }),
      map(() => true),
      catchError(() => of(false)),
    );
  }

  hasRole(role: string): boolean {
    return this.currentUserSignal()?.roles?.includes(`ROLE_${role}`) ?? false;
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some((role) => this.hasRole(role));
  }
}

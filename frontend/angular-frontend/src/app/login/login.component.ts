import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <main class="login-page">
      <section class="login-card">
        <h1>Skate Inventory Management</h1>
        <p>
          Sign in with Google to access the upgraded inventory platform with role-based access,
          OAuth session support, and AWS-ready backend configuration.
        </p>
        <button type="button" (click)="login()">Sign in with Google</button>
        <a routerLink="/products">View app</a>
      </section>
    </main>
  `,
  styles: [
    `
      .login-page {
        min-height: calc(100vh - 160px);
        display: grid;
        place-items: center;
        padding: 2rem;
      }
      .login-card {
        width: min(720px, 100%);
        background: #fff;
        border-radius: 20px;
        padding: 2rem;
        box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
        display: grid;
        gap: 1rem;
      }
      button {
        width: fit-content;
      }
    `,
  ],
})
export class LoginComponent {
  private readonly auth = inject(AuthService);

  login(): void {
    this.auth.login();
  }
}

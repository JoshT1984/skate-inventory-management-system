import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  template: `
    <main class="login-page">
      <section class="login-card">
        <span class="eyebrow">Skate Supply</span>
        <h1>Sign in to the inventory portal</h1>
        <p>
          Use your Google account to access warehouse tools, product data, and inventory actions.
        </p>

        <button type="button" class="google-btn" (click)="login()">Sign in with Google</button>

        <p class="helper-text">
          After sign-in, you will be routed directly to the warehouse inventory page.
        </p>
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
        background: linear-gradient(180deg, #f8fafc 0%, #eef2ff 100%);
      }

      .login-card {
        width: min(520px, 100%);
        background: #fff;
        border-radius: 24px;
        padding: 2.5rem;
        box-shadow: 0 20px 50px rgba(15, 23, 42, 0.12);
        display: grid;
        gap: 1rem;
        text-align: center;
      }

      .eyebrow {
        font-size: 0.85rem;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #4f46e5;
      }

      h1 {
        margin: 0;
        font-size: clamp(1.9rem, 3vw, 2.5rem);
        color: #0f172a;
      }

      p {
        margin: 0;
        color: #475569;
        line-height: 1.6;
      }

      .google-btn {
        justify-self: center;
        border: none;
        border-radius: 999px;
        padding: 0.9rem 1.4rem;
        font-size: 1rem;
        font-weight: 600;
        cursor: pointer;
        background: #111827;
        color: #fff;
      }

      .google-btn:hover {
        transform: translateY(-1px);
      }

      .helper-text {
        font-size: 0.95rem;
      }
    `,
  ],
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  login(): void {
    if (this.auth.isAuthenticated()) {
      this.router.navigate(['/warehouse-inventory']);
      return;
    }

    this.auth.login();
  }
}

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class HeaderComponent {
  isDrawerOpen = false;

  constructor(public auth: AuthService) {}

  toggleDrawer(): void {
    this.isDrawerOpen = !this.isDrawerOpen;
  }

  closeDrawer(): void {
    this.isDrawerOpen = false;
  }

  getInitial(): string {
    const name = this.displayName();
    return name ? name.charAt(0).toUpperCase() : 'U';
  }

  displayName(): string {
    const authAny = this.auth as any;

    const name =
      authAny.getDisplayName?.() ||
      authAny.user?.name ||
      authAny.user?.displayName ||
      authAny.user?.firstName ||
      authAny.currentUser?.name ||
      authAny.currentUser?.displayName ||
      authAny.profile?.name ||
      authAny.profile?.displayName ||
      authAny.email ||
      'User';

    return String(name);
  }

  login(): void {
    this.closeDrawer();
    this.auth.login();
  }

  logout(): void {
    this.closeDrawer();
    this.auth.logout();
  }
}

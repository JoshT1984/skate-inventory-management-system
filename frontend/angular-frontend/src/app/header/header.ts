import { CommonModule } from '@angular/common';
import { Component, HostListener, OnDestroy, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class HeaderComponent implements OnDestroy {
  readonly auth = inject(AuthService);
  isDrawerOpen = false;

  toggleDrawer(): void {
    this.isDrawerOpen ? this.closeDrawer() : this.openDrawer();
  }

  openDrawer(): void {
    this.isDrawerOpen = true;
    document.body.classList.add('no-scroll');
  }

  closeDrawer(): void {
    this.isDrawerOpen = false;
    document.body.classList.remove('no-scroll');
  }

  login(): void {
    this.auth.login();
  }

  logout(): void {
    this.auth.logout().subscribe();
  }

  displayName(): string {
    const user = this.auth.currentUser();
    return user ? `${user.firstName ?? ''} ${user.lastName ?? ''}`.trim() : 'Guest';
  }

  @HostListener('document:keydown.escape')
  onEsc(): void {
    if (this.isDrawerOpen) this.closeDrawer();
  }

  @HostListener('window:resize')
  onResize(): void {
    if (window.innerWidth >= 900 && this.isDrawerOpen) this.closeDrawer();
  }

  ngOnDestroy(): void {
    document.body.classList.remove('no-scroll');
  }
}

import { CommonModule } from '@angular/common';
import { Component, HostListener, OnDestroy } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class HeaderComponent implements OnDestroy {
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

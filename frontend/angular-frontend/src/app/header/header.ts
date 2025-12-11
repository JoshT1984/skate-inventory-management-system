import { Component } from '@angular/core';
import { FavoriteItemComponent } from '../favorite-item/favorite-item.component';
import { Nav } from './nav/nav';

@Component({
  selector: 'app-header',
  imports: [Nav, FavoriteItemComponent],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {}

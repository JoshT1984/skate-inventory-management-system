import { Component } from '@angular/core';
import { DataPassService } from '../services/data-pass.service';

@Component({
  selector: 'app-favorite-item',
  imports: [],
  templateUrl: './favorite-item.component.html',
  styleUrl: './favorite-item.component.css',
})
export class FavoriteItemComponent {
  favoriteItem: string = '';

  // in order to have access to the service passing the data
  // we have to inject the service

  constructor(private dataPass: DataPassService) {
    //assigning the value directly doesn't work
    //it only works once and doesn't see the changes
    // this.mockFavoriteItem = dataPass.favoriteItem;

    // if you have a single argument in subscribe()
    // it's a callback function that does something with the updated data
    this.dataPass.favoriteItem.subscribe((data) => {
      this.favoriteItem = data;
    });
  }
}

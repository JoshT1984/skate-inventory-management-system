import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Item } from '../models/item.model';
import { DataPassService } from '../services/data-pass.service';

@Component({
  selector: 'app-item',
  imports: [],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css',
})
export class ItemComponent {
  // to get data from the parent, we need to load Product info
  // into a local variable via the @Input() decorator
  @Input() item: Item = new Item(0, '', '', ''); //default data

  // @Output sends data up to the parent via event emitter
  @Output() deleteItemEvent = new EventEmitter<string>();

  favoriteItem: string = '';
  constructor(private dataPass: DataPassService) {
    this.dataPass.favoriteItem.subscribe((data) => {
      this.favoriteItem = data;
    });
  }

  deleteItem() {
    if (this.favoriteItem === this.item.name) {
      this.dataPass.setFavoriteItem('None');
    }
    this.deleteItemEvent.emit('Test Message...');
  }

  setFavoriteItem() {
    // This doesn't work correctly because dataPass doesn't know about this
    // this.dataPass.favoriteItem = this.item.name;

    this.dataPass.setFavoriteItem(this.item.name);
  }
}

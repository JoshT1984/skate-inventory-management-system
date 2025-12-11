import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

// the @Injectable decorator
// tells us we can "inject" this service into whatever needs it
// this injectable is a singleton -- only one instance will ever exist
// also the instance persists whether we're on a page that uses it or not

@Injectable({
  providedIn: 'root',
})
export class DataPassService {
  // we can't simply store values in this way
  // if we want other components to be aware that they've changed
  // favoriteItem: string = 'Service Item';

  //has a current state, and updates when state changes
  favoriteItemSubject = new BehaviorSubject<string>('');

  //we take our subject and create an Observable, so we can process the changes
  favoriteItem = this.favoriteItemSubject.asObservable();

  constructor() {}

  // this method tells the subject to update to the new state
  // and emit a change notification to its Observable
  setFavoriteItem(newFave: string) {
    this.favoriteItemSubject.next(newFave);
  }
}

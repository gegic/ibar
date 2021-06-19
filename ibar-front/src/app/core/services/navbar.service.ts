import {Injectable} from '@angular/core';
import {NavigationItem} from '../model/navigation-item';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NavbarService {
  hasSearch: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
  navigation: BehaviorSubject<NavigationItem[] | null> = new BehaviorSubject<NavigationItem[] | null>(null);
}

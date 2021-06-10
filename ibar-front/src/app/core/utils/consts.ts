import {NavigationItem} from '../model/navigation-item';

export const ADMIN = 'ROLE_ADMIN';
export const READER = 'ROLE_READER';

export const READER_NAVBAR: NavigationItem[] = [
  {routerLink: 'browse', icon: 'pi pi-table'},
  {routerLink: 'list', icon: 'pi pi-list'}
];

export const ADMIN_NAVBAR: NavigationItem[] = [

];

export const RESPONSIVE_OPTIONS = [
  {
    breakpoint: '2600px',
    numVisible: 10,
    numScroll: 2
  },
  {
    breakpoint: '2350px',
    numVisible: 8,
    numScroll: 2
  },
  {
    breakpoint: '1600px',
    numVisible: 5,
    numScroll: 1
  },
  {
    breakpoint: '1350px',
    numVisible: 4,
    numScroll: 1
  },
  {
    breakpoint: '1100px',
    numVisible: 3,
    numScroll: 1
  },
  {
    breakpoint: '850px',
    numVisible: 2,
    numScroll: 1
  },
  {
    breakpoint: '600px',
    numVisible: 1,
    numScroll: 1
  }
];

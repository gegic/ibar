export class NavigationItem {
  routerLink?: string;
  icon?: string;
  activeFunction?: ((url: string) => boolean);
}

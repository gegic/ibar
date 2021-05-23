import {Component, OnDestroy, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {Subscription} from 'rxjs';
import {AuthService} from '../../core/services/auth.service';
import {Router} from '@angular/router';
import {environment} from '../../../environments/environment';
import {TokenService} from '../../core/services/token.service';
import {Authority} from '../../core/model/authority';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  searchQuery = '';

  menuItems: MenuItem[] = [
    {label: 'Edit account', icon: 'pi pi-fw pi-user-edit', routerLink: ['/user-edit']},
    {label: 'Logout', icon: 'pi pi-fw pi-power-off', command: e => this.onClickLogout(), id: 'logout-btn'}
  ];

  constructor(private authService: AuthService,
              private router: Router,
              private tokenService: TokenService) { }

  ngOnInit(): void {
    console.log(this.tokenService.getToken());

    // this.subscriptions.push(
    //   this.culturalOfferingsService.searchQuery.subscribe(val => {
    //     this.searchQuery = val;
    //   })
    // );
  }

  onClickLogout(): void {
    this.tokenService.removeToken();
    this.router.navigate([environment.loginRoute]);
  }

  setSearchQuery(event: KeyboardEvent): void {

    if (event.key === 'Enter') {
      // do search
      // this.router.navigate(['list-view']);
    }
  }

  onClickSignIn(): void {
    this.router.navigate(['login']);
  }

  isLoggedIn(): boolean {
    return !!this.tokenService.getToken();
  }

  authoritiesContain(authorityName: string): boolean {
    return this.tokenService.getToken().authorities.some((au: Authority) => au.name === authorityName);
  }

  isLinkActive(url: string): boolean {
    const queryParamsIndex = this.router.url.indexOf('?');
    let baseUrl = queryParamsIndex === -1 ? this.router.url : this.router.url.slice(0, queryParamsIndex);
    if (baseUrl === url) {
      return true;
    }
    if (baseUrl.startsWith('/')) {
      baseUrl = baseUrl.slice(1);
    }
    if (baseUrl === url) {
      return true;
    }
    if (baseUrl.endsWith('/')) {
      baseUrl = baseUrl.slice(0, -1);
    }
    return baseUrl === url;
  }

  get initials(): string {
    return this.tokenService.getToken().userInitials;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}

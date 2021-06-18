import { AfterViewInit, Component, DoCheck, OnDestroy, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Subscription } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { TokenService } from '../../core/services/token.service';
import { Authority } from '../../core/model/authority';
import { NavigationItem } from '../../core/model/navigation-item';
import { NavbarService } from '../../core/services/navbar.service';
import { BookService } from '../../core/services/book.service';
import {Rank} from '../../core/model/rank';
import {RankService} from '../../core/services/rank.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  public isChangePasswordDialogOpen = false;

  searchQuery = '';

  navbarItems: NavigationItem[] = [];
  hasSearch = false;

  menuItems: MenuItem[] = [
    { label: 'Edit account', icon: 'pi pi-fw pi-user-edit', routerLink: ['/user-edit'] },
    { label: 'Change password', icon: 'pi pi-fw pi-key', command: e => this.onClickChangePassword(), id: 'change-password-btn' },
    { label: 'Logout', icon: 'pi pi-fw pi-power-off', command: e => this.onClickLogout(), id: 'logout-btn' }
  ];

  constructor(
    private authService: AuthService,
    private router: Router,
    private tokenService: TokenService,
    private navbarService: NavbarService,
    private bookService: BookService,
    private rankService: RankService) { }

  ngOnInit(): void {
    this.navbarService.navigation.subscribe(
      (val: NavigationItem[] | null) => {
        this.navbarItems = val ?? [];
      }
    );
    this.navbarService.hasSearch.subscribe(
      (val: boolean) => {
        this.hasSearch = val;
      }
    );
    this.rankService.getUserRank().subscribe(
      (val: Rank) => {
        this.rankService.userRank.next(val);
      }
    )
  }

  onClickLogout(): void {
    this.tokenService.removeToken();
    this.router.navigate([environment.loginRoute]);
  }

  onClickChangePassword(): void {
    this.isChangePasswordDialogOpen = true;
  }

  setSearchQuery(event: KeyboardEvent): void {

    if (event.key === 'Enter') {
      this.bookService.searchQuery.next(this.searchQuery);
      this.router.navigate(['list']);
    }
  }

  onClickSignIn(): void {
    this.router.navigate(['login']);
  }

  isLoggedIn(): boolean {
    return !!this.tokenService.getToken();
  }

  authoritiesContain(authorityName: string): boolean {
    return !!this.tokenService.getToken()?.authorities?.some((au: Authority) => au.name === authorityName);
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

  closeDialog(): void {
    this.isChangePasswordDialogOpen = false;
  }

  get initials(): string | undefined {
    return this.tokenService.getToken()?.userInitials;
  }

  get userRankName(): string {
    return this.rankService.userRank.getValue()?.name ?? 'NAR';
  }

}

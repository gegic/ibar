import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {TokenService} from '../services/token.service';
import {environment} from '../../../environments/environment';
import {ADMIN, ADMIN_NAVBAR, READER, READER_NAVBAR} from '../utils/consts';
import { JwtHelperService } from '@auth0/angular-jwt';
import {Authority} from '../model/authority';
import {NavbarService} from '../services/navbar.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private jwtHelper: JwtHelperService = new JwtHelperService();

  constructor(private tokenService: TokenService,
              private router: Router,
              private navbarService: NavbarService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const token: string | null = this.tokenService.getToken()?.accessToken;
    const isTokenExpired: boolean = !!token ? this.jwtHelper.isTokenExpired(token) : true;

    if (!isTokenExpired) {
      for (const role of route.data.roles || []){
        if (this.tokenService.getToken()?.authorities?.some((au: Authority) => au.name === role)){
          if (this.tokenService.getToken()?.authorities?.some((au: Authority) => au.name === ADMIN)) {
            this.navbarService.navigation.next(ADMIN_NAVBAR);
            this.navbarService.hasSearch.next(true);
          } else if (this.tokenService.getToken()?.authorities?.some((au: Authority) => au.name === READER)) {
            this.navbarService.navigation.next(READER_NAVBAR);
            this.navbarService.hasSearch.next(true);
          }
          return true;
        }
      }
      if (this.tokenService.getToken()?.authorities?.some((au: Authority) => au.name === ADMIN)) {
        this.router.navigate(['list']);
      } else {
        this.router.navigate(['']);
      }
    } else {
      if (route.data.unauthorized) {
        return true;
      }
      this.tokenService.removeToken();
      this.router.navigate([environment.loginRoute]);
    }

    return false;

  }

}

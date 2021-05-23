import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {TokenService} from '../services/token.service';
import {environment} from '../../../environments/environment';
import {ADMIN} from '../utils/consts';
import { JwtHelperService } from '@auth0/angular-jwt';
import {Authority} from '../model/authority';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private jwtHelper: JwtHelperService = new JwtHelperService();

  constructor(private tokenService: TokenService,
              private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const token: string | null = this.tokenService.getToken()?.accessToken;
    console.log(token);
    const isTokenExpired: boolean = !!token ? this.jwtHelper.isTokenExpired(token) : true;

    if (!isTokenExpired) {
      for (const role of route.data.roles || []){
        if (this.tokenService.getToken()?.authorities?.some((au: Authority) => au.name === role)){
          return true;
        }
      }
      this.router.navigate(['']);
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

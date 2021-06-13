import { Injectable } from '@angular/core';
import {AuthToken} from '../model/auth-token';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }

  readonly TOKEN_KEY = 'auth';

  getToken(): AuthToken {
    return JSON.parse(localStorage.getItem(this.TOKEN_KEY) ?? '');
  }

  setToken(token: AuthToken): void{
    localStorage.setItem(this.TOKEN_KEY, JSON.stringify(token));
  }

  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
}

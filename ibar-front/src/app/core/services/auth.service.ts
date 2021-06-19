import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {AuthToken} from '../model/auth-token';
import {HttpClient} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {User} from '../model/user';
import {Login} from '../model/login';
import {Activation} from '../model/activation';
import {ChangePassword} from '../model/changePassword';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  private readonly AUTH_PATH = 'auth';

  login(login: Login): Observable<AuthToken> {
    return this.httpClient.post<AuthToken>(`${this.AUTH_PATH}/login`, login);
  }

  changePassword(changePassword: ChangePassword): Observable<boolean> {
    return this.httpClient.post<boolean>(`${this.AUTH_PATH}/change-password`, changePassword);
  }
  registration(user: User): Observable<any> {
    return this.httpClient.post(`${this.AUTH_PATH}/sign-up`, user);
  }

  activate(activation: Activation): Observable<User | null> {
    return this.httpClient.post<User | null>(`${this.AUTH_PATH}/activate`, activation).pipe(
      catchError(() => of(null))
    );
  }

  getDisabled(uuid: string): Observable<User | null> {
    return this.httpClient.get<User | null>(`${this.AUTH_PATH}/disabled/${uuid}`).pipe(
      catchError(() => of(null))
    );
  }
}

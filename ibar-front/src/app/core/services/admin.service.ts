import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { User } from '../model/user';

@Injectable({
    providedIn: 'root'
})
export class AdminService {

    private readonly ADMIN_API = 'api/admins';

    public admins: User[] = [];

    constructor(private httpClient: HttpClient) { }

    public getAdmins(): Observable<User[]> {
        return this.httpClient.get<User[]>(`${this.ADMIN_API}`);
    }

    public delete(id: string): Observable<boolean> {
        return this.httpClient.delete<boolean>(`${this.ADMIN_API}/${id}`);
    }

    public create(user: User): Observable<User> {
        return this.httpClient.post<User>(`${this.ADMIN_API}`, user);
    }

    public update(id: string, user: User): Observable<User> {
        return this.httpClient.put<User>(`${this.ADMIN_API}/${id}`, user);
    }

}
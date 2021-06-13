import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Author } from '../model/author';

@Injectable({
    providedIn: 'root'
})
export class AuthorService {

    private readonly AUTHOR_API = 'api/authors';

    public authors: Author[] = [];

    constructor(private httpClient: HttpClient) { }

    public getAuthors(): Observable<Author[]> {
        return this.httpClient.get<Author[]>(`${this.AUTHOR_API}`);
    }

    public delete(id: string): Observable<boolean> {
        return this.httpClient.delete<boolean>(`${this.AUTHOR_API}/${id}`);
    }

    public create(data: FormData): Observable<Author> {
        return this.httpClient.post<Author>(`${this.AUTHOR_API}`, data);
    }

    public update(id: string, data: FormData): Observable<Author> {
        return this.httpClient.put<Author>(`${this.AUTHOR_API}/${id}`, data);
    }

}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';

import {Category} from '../model/category';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {

    private readonly CATEGORY_API = 'api/categories';

    public categories: Category[] = [];

    constructor(private httpClient: HttpClient) { }

    public getCategories(): Observable<Category[]> {
        return this.httpClient.get<Category[]>(`${this.CATEGORY_API}`);
    }

    public delete(id: string): Observable<boolean> {
        return this.httpClient.delete<boolean>(`${this.CATEGORY_API}/${id}`);
    }

    public create(category: Category): Observable<Category> {
        return this.httpClient.post<Category>(`${this.CATEGORY_API}`, category);
    }

    public update(id: string, category: Category): Observable<Category> {
        return this.httpClient.put<Category>(`${this.CATEGORY_API}/${id}`, category);
    }

}

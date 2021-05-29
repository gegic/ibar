import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Book} from '../model/book';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private readonly BOOK_API = 'api/books';

  constructor(private httpClient: HttpClient) { }

  getTopRated(): Observable<Book[]> {
    return this.httpClient.get<Book[]>(`${this.BOOK_API}/top-rated`);
  }

  getRecommended(): Observable<Book[]> {
    return this.httpClient.get<Book[]>(`${this.BOOK_API}/recommended`);
  }
}

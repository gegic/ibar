import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Book} from '../model/book';
import {HttpClient} from '@angular/common/http';
import {Filter} from './filter';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  books: BehaviorSubject<Book[]> = new BehaviorSubject<Book[]>([]);
  searchQuery: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  filter: BehaviorSubject<Filter> = new BehaviorSubject<Filter>(new Filter());

  private readonly BOOK_API = 'api/books';

  constructor(private httpClient: HttpClient) { }

  getBooks(filter: Filter): Observable<Book[]> {
    let api = `${this.BOOK_API}/search`;
    if (!!this.searchQuery.getValue()) {
      api += `?query=${this.searchQuery.getValue()}`;
    }
    return this.httpClient.post<Book[]>(api, filter);
  }

  getTopRated(): Observable<Book[]> {
    return this.httpClient.get<Book[]>(`${this.BOOK_API}/top-rated`);
  }

  getRecommended(): Observable<Book[]> {
    return this.httpClient.get<Book[]>(`${this.BOOK_API}/recommended`);
  }
}

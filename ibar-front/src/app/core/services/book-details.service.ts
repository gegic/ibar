import { Injectable } from '@angular/core';
import {Book} from '../model/book';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookDetailsService {

  private readonly API_URL = '/api/books';

  book: BehaviorSubject<Book | undefined> = new BehaviorSubject<Book | undefined>(undefined);

  constructor(private httpClient: HttpClient) { }

  getBook(id: number): Observable<Book> {
    return this.httpClient.get<Book>(`${this.API_URL}/${id}`);
  }
}

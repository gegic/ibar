import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Book} from '../model/book';
import {HttpClient} from '@angular/common/http';
import {Filter} from './filter';
import {Cover} from '../model/cover';
import {ContentFile} from '../model/content-file';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  book: BehaviorSubject<Book | null> = new BehaviorSubject<Book | null>(null);
  books: BehaviorSubject<Book[]> = new BehaviorSubject<Book[]>([]);
  searchQuery: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  filter: BehaviorSubject<Filter> = new BehaviorSubject<Filter>(new Filter());

  private readonly BOOK_API = 'api/books';

  constructor(private httpClient: HttpClient) { }

  getBook(id: string): Observable<Book> {
    return this.httpClient.get<Book>(`${this.BOOK_API}/${id}`);
  }

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

  addBook(book: Book): Observable<Book> {
    return this.httpClient.post<Book>(`${this.BOOK_API}`, book);
  }

  editBook(book: Book): Observable<Book> {
    return this.httpClient.put<Book>(`${this.BOOK_API}`, book);
  }

  addCover(file: File): Observable<Cover> {
    const formData = new FormData();
    formData.append('cover', file);
    return this.httpClient.post<Cover>(`${this.BOOK_API}/add-cover`, formData);
  }

  addPdf(file: File): Observable<ContentFile> {
    const formData = new FormData();
    formData.append('pdf', file);
    return this.httpClient.post<ContentFile>(`${this.BOOK_API}/add-pdf`, formData);
  }

  delete(bookId: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.BOOK_API}/${bookId}`);
  }
}

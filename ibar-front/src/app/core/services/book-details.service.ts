import {Injectable} from '@angular/core';
import {Book} from '../model/book';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {ReadingProgress} from '../model/reading-progress';

@Injectable({
  providedIn: 'root'
})
export class BookDetailsService {

  private readonly BOOKS_API = '/api/books';
  private readonly READING_PROGRESS_API = 'api/reading-progress';

  book: BehaviorSubject<Book | undefined> = new BehaviorSubject<Book | undefined>(undefined);
  readingProgress: BehaviorSubject<ReadingProgress | undefined> = new BehaviorSubject<ReadingProgress | undefined>(undefined);

  constructor(private httpClient: HttpClient) { }

  getBook(id: string): Observable<Book> {
    return this.httpClient.get<Book>(`${this.BOOKS_API}/${id}`);
  }

  getReadingProgress(bookId: string): Observable<ReadingProgress> {
    return this.httpClient.get<ReadingProgress>(`${this.READING_PROGRESS_API}/book/${bookId}`);
  }

  postReadingProgress(progress: ReadingProgress): Observable<ReadingProgress> {
    return this.httpClient.post<ReadingProgress>(`${this.READING_PROGRESS_API}`, progress);
  }
}

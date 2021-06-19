import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ReadingListItem} from '../model/reading-list-item';

@Injectable({
  providedIn: 'root'
})
export class ReadingListService {

  private readonly READING_LIST_API = '/api/reading-list';

  constructor(private httpClient: HttpClient) { }

  addToReadingList(bookId: string): Observable<ReadingListItem> {
    return this.httpClient.post<ReadingListItem>(`${this.READING_LIST_API}/book/${bookId}`, null);
  }

  removeFromReadingList(bookId: string): Observable<ReadingListItem> {
    return this.httpClient.delete<ReadingListItem>(`${this.READING_LIST_API}/book/${bookId}`);
  }

}

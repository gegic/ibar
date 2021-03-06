import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../model/page';
import {Review} from '../model/review';
import {ReviewNumber} from '../model/review-number-list';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private httpClient: HttpClient) { }

  getReviews(bookId: string, page: number): Observable<Page<Review>> {
    return this.httpClient.get<Page<Review>>(`/api/reviews/book/${bookId}?page=${page}`);
  }

  getReviewNumbers(bookId: string): Observable<ReviewNumber[]> {
    return this.httpClient.get<ReviewNumber[]>(`/api/reviews/by-rating/book/${bookId}`);
  }

  getReviewForUser(bookId: string): Observable<Review> {
    return this.httpClient.get<Review>(`/api/reviews/user/book/${bookId}`);
  }

  delete(reviewId: string): Observable<void> {
    return this.httpClient.delete<void>(`/api/reviews/${reviewId}`);
  }

  post(review: Review): Observable<Review> {
    return this.httpClient.post<Review>('/api/reviews', review);
  }

  put(review: Review): Observable<Review> {
    return this.httpClient.put<Review>('/api/reviews', review);
  }

}

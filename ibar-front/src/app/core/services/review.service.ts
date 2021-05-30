import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../model/page';
import {Review} from '../model/review';
import {ReviewNumber} from '../model/review-number-list';
import {ReviewNumbers} from '../model/review-numbers';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private httpClient: HttpClient) { }

  getReviews(bookId: number, page: number): Observable<Page<Review>> {
    return this.httpClient.get<Page<Review>>(`/api/reviews/book/${bookId}?page=${page}`);
  }

  getReviewNumbers(bookId: number): Observable<ReviewNumber[]> {
    return this.httpClient.get<ReviewNumber[]>(`/api/reviews/by-rating/book/${bookId}`);
  }

  getReviewForUser(bookId: number): Observable<Review> {
    return this.httpClient.get<Review>(`/api/reviews/user/book/${bookId}`);
  }

  delete(reviewId: number): Observable<void> {
    return this.httpClient.delete<void>(`/api/reviews/${reviewId}`);
  }

  add(review: Review): Observable<Review> {
    return this.httpClient.post<Review>('/api/reviews', review);
  }

  edit(review: Review): Observable<Review> {
    return this.httpClient.put<Review>('/api/reviews', review);
  }

}

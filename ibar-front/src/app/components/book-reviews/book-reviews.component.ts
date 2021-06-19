import {Component, OnInit} from '@angular/core';
import {Book} from '../../core/model/book';
import {BookDetailsService} from '../../core/services/book-details.service';
import {ReviewNumbers} from '../../core/model/review-numbers';
import {Review} from '../../core/model/review';
import {ReviewService} from '../../core/services/review.service';
import {Page} from '../../core/model/page';
import {TokenService} from '../../core/services/token.service';
import {ReviewNumber} from '../../core/model/review-number-list';
import {READER} from '../../core/utils/consts';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {ReadingProgress} from '../../core/model/reading-progress';

@Component({
  selector: 'app-book-reviews',
  templateUrl: './book-reviews.component.html',
  styleUrls: ['./book-reviews.component.scss']
})
export class BookReviewsComponent implements OnInit {

  page = -1;
  totalPages = 0;
  isAddDialogOpen = false;
  userReview = new Review();
  isReviewsLoading = false;
  reviewNumbers = new ReviewNumbers();
  reviews: Review[] = [];

  constructor(private detailsService: BookDetailsService,
              private reviewService: ReviewService,
              private tokenService: TokenService,
              private router: Router,
              private messageService: MessageService) { }

  ngOnInit(): void {
    this.detailsService.book.subscribe((val: Book | undefined) => {
      if (!!val) {
        this.resetReviews();
      }
    });
  }

  resetReviews(): void {
    this.isReviewsLoading = true;
    this.reviews = [];
    this.reviewNumbers = new ReviewNumbers();
    this.page = -1;
    this.totalPages = 0;
    this.getReviews();
    this.getReviewNumbers();
    this.getUserReview();
  }

  getReviews(): void {
    if (this.page === this.totalPages) {
      this.isReviewsLoading = false;
      return;
    }
    this.isReviewsLoading = true;
    this.reviewService.getReviews(this.book?.id ?? '', this.page + 1)
      .subscribe((reviewPage: Page<Review>) => {
        const userId = this.tokenService.getToken()?.userId ?? null;
        let addedReviews: Review[];
        if (!!userId) {
          addedReviews = reviewPage.content?.filter(rv => rv.userId !== userId) ?? [];
        } else {
          addedReviews = reviewPage.content ?? [];
        }
        this.reviews = addedReviews;
        this.page = reviewPage.pageable?.pageNumber ?? 0;
        this.totalPages = reviewPage?.totalPages ?? 0;
        this.isReviewsLoading = false;
      });
  }

  getReviewNumbers(): void {
    this.isReviewsLoading = true;
    this.reviewService.getReviewNumbers(this.book?.id ?? '').subscribe((rn: ReviewNumber[]) => {
      for (const reviewNum of rn) {
        switch (reviewNum.rating) {
          case 1:
            this.reviewNumbers.oneStar = reviewNum?.numReviews ?? 0;
            break;
          case 2:
            this.reviewNumbers.twoStars = reviewNum?.numReviews ?? 0;
            break;
          case 3:
            this.reviewNumbers.threeStars = reviewNum?.numReviews ?? 0;
            break;
          case 4:
            this.reviewNumbers.fourStars = reviewNum?.numReviews ?? 0;
            break;
          case 5:
            this.reviewNumbers.fiveStars = reviewNum?.numReviews ?? 0;
            break;
          default:
        }
      }
      this.isReviewsLoading = false;
    });
  }

  getUserReview(): void {
    this.isReviewsLoading = true;
    this.reviewService.getReviewForUser(this.book?.id ?? '').subscribe((val: Review) => {
      if (!!val) {
        this.userReview = val;
        this.isReviewsLoading = false;
      }
    });
  }

  onClickSend(): void {
    if (!this.userReview.rating || this.userReview.rating < 1 || this.userReview.rating > 5) {
      this.messageService.add({
        severity: 'error',
        summary: 'Rating is missing',
        detail: 'Review rating is required, whereas other fields are optional.'
      });
      return;
    }

    this.userReview.userId = this.tokenService.getToken()?.userId;
    this.userReview.bookId = this.book?.id ?? '';

    if (!this.userReview.userId || !this.userReview.bookId) {
      this.messageService.add({
        severity: 'error',
        summary: 'Unexpected error',
        detail: 'An unexpected error occurred. Please refresh the page.'
      });
      return;
    }
    if (!this.userReview.id) {
      this.reviewService.post(this.userReview).subscribe(
        addedReview => {
          this.resetOffering();
          this.resetReviews();
          this.userReview = addedReview;
          this.isAddDialogOpen = false;
        },
        () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Blocked account',
            detail: 'Your account has been permanently blocked'
          });
          this.tokenService.removeToken();
          this.router.navigate(['login']);
        }
      );
    } else {
      this.reviewService.put(this.userReview).subscribe(editedReview => {
        this.resetOffering();
        this.resetReviews();
        this.userReview = editedReview;
        this.isAddDialogOpen = false;
      });
    }
  }

  resetOffering(): void {
    this.detailsService.getBook(this.book?.id ?? '').subscribe(
      val => {
        this.detailsService.book.next(val);
      }
    );
  }

  onReviewDeleted(): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Review deleted',
      detail: 'The review was deleted successfully.'
    });
    this.resetOffering();
    this.resetReviews();
  }

  onWriteReviewClick(): void {
    if (!this.tokenService.getToken()) {
      this.router.navigate(['login']);
    }
    this.isAddDialogOpen = true;
  }

  clearNewReview(): void {
    if (!this.userReview.id) {
      this.userReview = new Review();
    } else {
      this.getUserReview();
    }
  }

  getReviewNumberPercentage(numReviews: number): number {
    return numReviews / (this.book?.numReviews ?? 1) * 100;
  }

  get canAddReview(): boolean {
    return !!(this.tokenService.getToken()?.authorities?.some(au => au.name === READER) &&
      !!this.readingProgress && (this.readingProgress.percentage ?? 0) > 80);
  }

  get readingProgress(): ReadingProgress | undefined {
    return this.detailsService.readingProgress.getValue();
  }

  get overallRating(): string {
    return (Math.round((this.book?.averageRating ?? 0) * 10) / 10).toFixed(1);
  }

  get book(): Book | undefined{
    return this.detailsService.book.getValue();
  }
}

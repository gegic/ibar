import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Review} from '../../core/model/review';
import {ConfirmationService, MenuItem} from 'primeng/api';
import {ReviewService} from '../../core/services/review.service';
import {TokenService} from '../../core/services/token.service';
import {TimeUtil} from '../../core/utils/time-util';

@Component({
  selector: 'app-book-review-element',
  templateUrl: './book-review-element.component.html',
  styleUrls: ['./book-review-element.component.scss']
})
export class BookReviewElementComponent implements OnInit {

  @Input()
  review?: Review;
  @Output()
  reviewDeleted: EventEmitter<any> = new EventEmitter<any>();

  menuItems: MenuItem[] = [{ label: 'Delete', command: () => this.deleteReview() }];

  constructor(private confirmationService: ConfirmationService,
              private reviewService: ReviewService,
              private tokenService: TokenService) { }

  ngOnInit(): void {
  }

  deleteReview(): void {
    this.confirmationService.confirm(
      {
        message: 'Are you sure that you want to delete this review',
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.reviewDeletionConfirmed()
      });
  }

  reviewDeletionConfirmed(): void {
    if (!this.review || !this.review.id) {
      return;
    }
    this.reviewService.delete(this.review.id).subscribe(() => {
      this.reviewDeleted.emit();
    });
  }

  getAddedAgoString(): string {
    if (!this.review?.timeAdded) {
      return 'Some time ago';
    }
    const now = Date.now();
    const timeAddedMs = new Date(this.review.timeAdded);
    return TimeUtil.timeDifference(now.valueOf(), timeAddedMs.valueOf());
  }

}

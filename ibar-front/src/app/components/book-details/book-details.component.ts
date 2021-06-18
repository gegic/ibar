import {Component, OnDestroy, OnInit} from '@angular/core';
import {Book} from '../../core/model/book';
import {TokenService} from '../../core/services/token.service';
import {ADMIN, READER} from '../../core/utils/consts';
import {distinctUntilChanged} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {BookDetailsService} from '../../core/services/book-details.service';
import {Title} from '@angular/platform-browser';
import {ReadingProgress} from '../../core/model/reading-progress';
import {ConfirmationService} from 'primeng/api';
import {BookService} from '../../core/services/book.service';
import {ReadingListService} from '../../core/services/reading-list.service';
import {ReadingListItem} from '../../core/model/reading-list-item';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.scss']
})
export class BookDetailsComponent implements OnInit, OnDestroy {

  readonly navigationItems = [
    {
      label: 'About',
      link: 'about',
      icon: 'pi pi-info-circle'
    },
    {
      label: 'Reviews',
      link: 'reviews',
      icon: 'pi pi-star'
    }
  ];

  isLoading = true;
  readingListLoading = false;

  constructor(private tokenService: TokenService,
              private activatedRoute: ActivatedRoute,
              private detailsService: BookDetailsService,
              private titleService: Title,
              private router: Router,
              private confirmationService: ConfirmationService,
              private bookService: BookService,
              private readingListService: ReadingListService) { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(distinctUntilChanged()).subscribe(
      val => {
        if (!!val.id) {
          this.getBook(val.id);
          this.getReadingProgress(val.id);
        } else {
          this.router.navigate(['']);
        }
      });
  }

  getBook(id: string): void {
    this.isLoading = true;
    this.detailsService.getBook(id).pipe(distinctUntilChanged()).subscribe(
      data => {
        this.detailsService.book.next(data);
        const newTitle = `${data.name} | ibar`;
        this.titleService.setTitle(newTitle);
        this.isLoading = false;
      }
    );
  }

  getReadingProgress(id: string): void {
    this.isLoading = true;
    this.detailsService.getReadingProgress(id).subscribe(
      (data: ReadingProgress) => {
        this.detailsService.readingProgress.next(data);
        this.isLoading = false;
      }
    );
  }


  onClickEdit(): void {
    this.router.navigate(['edit-book', this.book?.id ?? '']);
  }

  onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.book?.name}`,
        header: 'Delete',
        accept: () => this.bookDeleted()
      }
    );
  }

  bookDeleted(): void {
    this.bookService.delete(this.book?.id ?? '').subscribe(
      () => {
        this.router.navigate(['list']);
      }
    );
  }

  onClickAddToReadingList(): void {
    this.readingListLoading = true;
    this.readingListService.addToReadingList(this.book?.id ?? '').subscribe(
      () => {
        if (this.book) {
          this.book.inReadingList = true;
        }
        this.readingListLoading = false;
      }
    );
  }

  onClickRemoveFromReadingList(): void {
    this.readingListLoading = true;
    this.readingListService.removeFromReadingList(this.book?.id ?? '').subscribe(
      () => {
        if (this.book) {
          this.book.inReadingList = false;
        }
        this.readingListLoading = false;
      }
    );
  }

  onClickRead(): void {
    this.router.navigate(['reading', this.book?.id ?? '']);
  }

  get canRead(): boolean {
    return (!!this.tokenService.getToken()?.authorities?.some(au => au.name === READER)) && !!this.book?.pdf;
  }

  get canModify(): boolean {
    return !!this.tokenService.getToken()?.authorities?.some(au => au.name === ADMIN);
  }

  get readingProgress(): ReadingProgress | undefined {
    return this.detailsService.readingProgress.getValue();
  }

  get reviews(): string {
    if (this.book?.numReviews === 0) {
      return 'No reviews so far.';
    } else {
      return `${(Math.round((this.book?.averageRating ?? 0) * 10) / 10).toFixed(1)} from ${this.book?.numReviews} reviews.`;
    }
  }

  get book(): Book | undefined {
    return this.detailsService.book.getValue();
  }

  ngOnDestroy(): void {
    this.titleService.setTitle('ibar');
  }

}

import { Component, OnInit } from '@angular/core';
import {BookService} from '../../core/services/book.service';
import {TokenService} from '../../core/services/token.service';
import {Book} from '../../core/model/book';
import {Filter} from '../../core/services/filter';
import {ADMIN} from '../../core/utils/consts';
import {RatingInterval} from '../../core/services/rating-interval';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {

  searchQuery = '';

  filter: Filter = new Filter();

  isFilterDialogOpen = false;
  filterSet = false;
  isLoadingBooks = false;

  constructor(private bookService: BookService,
              private tokenService: TokenService) { }

  ngOnInit(): void {
    this.bookService.searchQuery.subscribe(() => {
      this.resetBooks();
    });
    this.getBooks();
  }

  getBooks(): void {
    this.isLoadingBooks = true;
    this.filter.ratingInterval.applyInterval();
    this.bookService.getBooks(this.filter).subscribe(
      (books: Book[]) => {
        this.bookService.books.next(books);
        this.isLoadingBooks = false;
      }
    );
  }

  openFilterDialog(): void {
    this.isFilterDialogOpen = true;
  }

  resetFilter(): void {
    this.filterSet = false;
    this.filter.ratingInterval = new RatingInterval();
    this.filter.authorsName = null;
    this.bookService.filter.next(this.filter);
    this.resetBooks();
  }

  restoreFilter(): void {
    this.filter = this.bookService.filter.getValue();
  }

  resetBooks(): void {
    this.isLoadingBooks = true;
    this.bookService.books.next([]);
    this.getBooks();
  }

  saveFilter(reset?: boolean): void {
    if (!reset) {
      this.filterSet = true;
    }
    this.bookService.filter.next({
      ratingInterval: new RatingInterval(),
      authorsName: null
    });
    this.resetBooks();
    this.isFilterDialogOpen = false;
  }

  bookDeleted(): void {
  }

  get canModify(): boolean {
    return !!this.tokenService.getToken()?.authorities?.some(au => au.name === ADMIN);
  }

  get books(): Book[] {
    return this.bookService.books.getValue();
  }
}

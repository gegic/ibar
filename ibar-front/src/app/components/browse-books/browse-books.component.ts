import { Component, OnInit } from '@angular/core';
import {Book} from '../../core/model/book';
import {BookService} from '../../core/services/book.service';
import {TokenService} from '../../core/services/token.service';
import {RESPONSIVE_OPTIONS} from '../../core/utils/consts';

@Component({
  selector: 'app-browse-books',
  templateUrl: './browse-books.component.html',
  styleUrls: ['./browse-books.component.scss']
})
export class BrowseBooksComponent implements OnInit {

  topRated: Book[] = [];
  loadingTopRated = true;

  recommended: Book[] = [];
  loadingRecommended = true;
  responsiveOptions = RESPONSIVE_OPTIONS;

  constructor(private bookService: BookService,
              private tokenService: TokenService) { }

  ngOnInit(): void {
    this.getTopRatedBooks();
    this.getRecommendedBooks();
  }

  getTopRatedBooks(): void {
    const userId = this.tokenService.getToken().userId;
    this.loadingTopRated = true;
    this.bookService.getTopRated().subscribe((books: Book[]) => {
      this.topRated = books;
      this.loadingTopRated = false;
    });
  }

  getRecommendedBooks(): void {
    const userId = this.tokenService.getToken().userId;
    this.loadingRecommended = true;
    this.bookService.getRecommended().subscribe((books: Book[]) => {
      this.recommended = books;
      this.loadingRecommended = false;
    });
  }

}

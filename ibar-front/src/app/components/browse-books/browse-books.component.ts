import { Component, OnInit } from '@angular/core';
import {Book} from '../../core/model/book';
import {BookService} from '../../core/services/book.service';
import {TokenService} from '../../core/services/token.service';

@Component({
  selector: 'app-browse-books',
  templateUrl: './browse-books.component.html',
  styleUrls: ['./browse-books.component.scss']
})
export class BrowseBooksComponent implements OnInit {

  topRated: Book[];
  loadingTopRated = true;

  recommended: Book[];
  loadingRecommended = true;

  responsiveOptions = [
    {
      breakpoint: '2600px',
      numVisible: 10,
      numScroll: 2
    },
    {
      breakpoint: '2350px',
      numVisible: 8,
      numScroll: 2
    },
    {
      breakpoint: '1600px',
      numVisible: 5,
      numScroll: 1
    },
    {
      breakpoint: '1350px',
      numVisible: 4,
      numScroll: 1
    },
    {
      breakpoint: '1100px',
      numVisible: 3,
      numScroll: 1
    },
    {
      breakpoint: '850px',
      numVisible: 2,
      numScroll: 1
    },
    {
      breakpoint: '600px',
      numVisible: 1,
      numScroll: 1
    }
  ];

  constructor(private bookService: BookService,
              private tokenService: TokenService) { }

  ngOnInit(): void {
    this.getTopRatedBooks();
    this.getRecommendedBooks();
  }

  getTopRatedBooks(): void {
    const userId = this.tokenService.getToken().userId;
    this.loadingTopRated = true;
    this.bookService.getTopRated(userId).subscribe((books: Book[]) => {
      this.topRated = books;
      this.loadingTopRated = false;
    });
  }

  getRecommendedBooks(): void {
    const userId = this.tokenService.getToken().userId;
    this.loadingRecommended = true;
    this.bookService.getRecommended(userId).subscribe((books: Book[]) => {
      this.recommended = books;
      this.loadingRecommended = false;
    });
  }

}

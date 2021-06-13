import {Component, Input, OnInit} from '@angular/core';
import {Book} from '../../core/model/book';
import {Router} from '@angular/router';

@Component({
  selector: 'app-book-cover',
  templateUrl: './book-cover.component.html',
  styleUrls: ['./book-cover.component.scss']
})
export class BookCoverComponent {

  @Input()
  book: Book = new Book();

  constructor(private router: Router) { }

  openDetails(): void {
    this.router.navigate(['/book', this.book.id]);
  }

}

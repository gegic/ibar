import {Component, Input, OnInit} from '@angular/core';
import {Book} from '../../core/model/book';

@Component({
  selector: 'app-book-cover',
  templateUrl: './book-cover.component.html',
  styleUrls: ['./book-cover.component.scss']
})
export class BookCoverComponent implements OnInit {

  @Input()
  book: Book;

  constructor() { }

  ngOnInit(): void {
  }

}

import { Component, OnInit } from '@angular/core';
import {Book} from '../../core/model/book';
import {BookDetailsService} from '../../core/services/book-details.service';

@Component({
  selector: 'app-book-about',
  templateUrl: './book-about.component.html',
  styleUrls: ['./book-about.component.scss']
})
export class BookAboutComponent {

  constructor(private detailsService: BookDetailsService) { }

  get book(): Book {
    return this.detailsService.book.getValue();
  }
}

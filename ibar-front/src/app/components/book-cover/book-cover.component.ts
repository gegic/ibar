import {Component, Input, OnInit} from '@angular/core';
import {Book} from '../../core/model/book';
import {Router} from '@angular/router';

@Component({
  selector: 'app-book-cover',
  templateUrl: './book-cover.component.html',
  styleUrls: ['./book-cover.component.scss']
})
export class BookCoverComponent implements OnInit{

  COVERS_API = '/covers';

  @Input()
  book: Book = new Book();

  topStyle?: {background: string};

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.topStyle = {
      background: `url(${this.COVERS_API}/${this.book.cover}.png) no-repeat center center`
    };
  }

  openDetails(): void {
    this.router.navigate(['/book', this.book.id]);
  }

}

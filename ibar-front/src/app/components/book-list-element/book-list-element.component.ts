import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Book} from '../../core/model/book';
import {Router} from '@angular/router';
import {BookService} from '../../core/services/book.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {TokenService} from '../../core/services/token.service';
import {ADMIN} from '../../core/utils/consts';

@Component({
  selector: 'app-book-list-element',
  templateUrl: './book-list-element.component.html',
  styleUrls: ['./book-list-element.component.scss']
})
export class BookListElementComponent implements OnInit {

  @Input()
  book: Book = new Book();
  @Input()
  index = 0;
  @Output()
  bookDeleted: EventEmitter<any> = new EventEmitter<any>();

  constructor(private router: Router,
              private bookService: BookService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService,
              private tokenService: TokenService) { }

  ngOnInit(): void {
  }

  onClickCard(): void {
    this.router.navigate(['book', this.book.id]);
  }

  onClickRead(): void {
    this.router.navigate(['reading', this.book.pdf]);
  }

  onClickEdit(event: MouseEvent): void {
  }

  onClickDelete(event: MouseEvent): void {
  }

  get canModify(): boolean {
    return !!this.tokenService.getToken()?.authorities?.some(au => au.name === ADMIN);
  }

  get coverUrl(): string {
    return 'https://edit.org/img/blog/m68-book-cover-templates.jpg';
  }
}

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Book} from '../../core/model/book';
import {Router} from '@angular/router';
import {BookService} from '../../core/services/book.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {TokenService} from '../../core/services/token.service';
import {ADMIN} from '../../core/utils/consts';
import {ReadingListService} from '../../core/services/reading-list.service';

@Component({
  selector: 'app-book-list-element',
  templateUrl: './book-list-element.component.html',
  styleUrls: ['./book-list-element.component.scss']
})
export class BookListElementComponent implements OnInit {

  COVERS_API = '/covers';

  @Input()
  book: Book = new Book();
  @Input()
  index = 0;
  @Output()
  deleteClicked: EventEmitter<any> = new EventEmitter<any>();

  readingListLoading = false;

  constructor(private router: Router,
              private bookService: BookService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService,
              private tokenService: TokenService,
              private readingListService: ReadingListService) { }

  ngOnInit(): void {
  }

  onClickCard(): void {
    this.router.navigate(['book', this.book.id]);
  }

  onClickRead(): void {
    this.router.navigate(['reading', this.book.pdf]);
  }

  onClickEdit(event: MouseEvent): void {
    event.stopPropagation();
    this.router.navigate(['edit-book', this.book.id]);
  }

  onClickDelete(event: MouseEvent): void {
    event.stopPropagation();
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.book.name}`,
        header: 'Delete',
        accept: () => this.deleteClicked.emit(this.book)
      }
    );
  }

  onClickAddToReadingList(event: MouseEvent): void {
    event.stopPropagation();
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

  onClickRemoveFromReadingList(event: MouseEvent): void {
    event.stopPropagation();
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

  get canModify(): boolean {
    return !!this.tokenService.getToken()?.authorities?.some(au => au.name === ADMIN);
  }

  get coverUrl(): string {
    return `${this.COVERS_API}/${this.book.cover}.png`;
  }
}

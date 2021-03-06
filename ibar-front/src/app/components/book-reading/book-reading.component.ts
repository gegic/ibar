import {Component, OnDestroy, OnInit} from '@angular/core';
import {BookDetailsService} from '../../core/services/book-details.service';
import {Book} from '../../core/model/book';
import {distinctUntilChanged} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {ReadingProgress} from '../../core/model/reading-progress';
import {TokenService} from '../../core/services/token.service';

@Component({
  selector: 'app-book-reading',
  templateUrl: './book-reading.component.html',
  styleUrls: ['./book-reading.component.scss']
})
export class BookReadingComponent implements OnInit, OnDestroy {

  isLoading = true;
  currentPage = 0;

  constructor(private detailsService: BookDetailsService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private titleService: Title,
              private tokenService: TokenService) { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(distinctUntilChanged()).subscribe(
      val => {
        if (!!val.id && (!this.book || (!!this.book && this.book.id !== val.id))) {
          this.getBook(val.id);
          this.getReadingProgress(val.id);
        } else if (!!this.book && this.book.id === val.id) {
          this.getReadingProgress(val.id);
        } else {
          this.router.navigate(['']);
        }
      });
  }

  getBook(id: string): void {
    this.isLoading = true;
    this.detailsService.getBook(id).pipe(distinctUntilChanged()).subscribe(
      (data: Book) => {
        if (!data.pdf) {
          this.router.navigate(['']);
        }
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
        this.currentPage = this.readingProgress?.progress ?? 0;
        this.isLoading = false;
        setInterval(() => this.postProgress(),  3 * 60 * 1000);
      }
    );
  }

  postProgress(): void {
    if (this.currentPage > (this.readingProgress?.progress ?? 0)) {
      const readingProgress = this.readingProgress;
      if (!readingProgress) {
        return;
      }
      readingProgress.progress = this.currentPage;
      this.detailsService.postReadingProgress(readingProgress).subscribe(
        (data: ReadingProgress | undefined) => {
          this.detailsService.readingProgress.next(data);
        }
      );
    }
  }

  backToBook(): void {
    this.router.navigate(['book', this.book?.id ?? '']);
  }


  get book(): Book | undefined {
    return this.detailsService.book?.getValue();
  }

  get readingProgress(): ReadingProgress | undefined {
    return this.detailsService.readingProgress?.getValue();
  }

  get pdfUrl(): string {
    return `/pdf/${this.book?.pdf ?? ''}.pdf`;
  }

  get token(): string {
    return this.tokenService.getToken()?.accessToken ?? '';
  }

  ngOnDestroy(): void {
    this.titleService.setTitle('ibar');
  }
}

import { Component, OnInit } from '@angular/core';
import {BookDetailsService} from '../../core/services/book-details.service';
import {Book} from '../../core/model/book';
import {distinctUntilChanged} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {ReadingProgress} from '../../core/model/reading-progress';

@Component({
  selector: 'app-book-reading',
  templateUrl: './book-reading.component.html',
  styleUrls: ['./book-reading.component.scss']
})
export class BookReadingComponent implements OnInit {

  isLoading = true;
  currentPage = 1;

  constructor(private detailsService: BookDetailsService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private titleService: Title) { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(distinctUntilChanged()).subscribe(
      val => {
        if (!!val.id && (!this.book || (!!this.book && this.book.id !== val.id))) {
          this.getBook(val.id);
          this.getReadingProgress(val.id);
          setInterval(this.postProgress, 4 * 60 * 1000);
        } else {
          this.router.navigate(['']);
        }
      });
  }

  getBook(id: number): void {
    this.isLoading = true;
    this.detailsService.getBook(id).pipe(distinctUntilChanged()).subscribe(
      (data: Book) => {
        this.detailsService.book.next(data);
        const newTitle = `${data.name} | ibar`;
        this.titleService.setTitle(newTitle);
        this.isLoading = false;
      }
    );
  }

  getReadingProgress(id: number): void {
    this.isLoading = true;
    this.detailsService.getReadingProgress(id).subscribe(
      (data: ReadingProgress) => {
        this.detailsService.readingProgress.next(data);
        this.currentPage = this.readingProgress.progress;
        this.isLoading = false;
      }
    );
  }

  postProgress(): void {
    if (this.currentPage > this.readingProgress.progress) {
      const readingProgress = this.readingProgress;
      readingProgress.progress = this.currentPage;
      this.detailsService.postReadingProgress(readingProgress).subscribe(
        (data: ReadingProgress) => {
          this.detailsService.readingProgress.next(data);
        }
      );
    }
  }

  backToBook(): void {
    this.router.navigate(['book', this.book.id]);
  }


  get book(): Book {
    return this.detailsService.book.getValue();
  }

  get readingProgress(): ReadingProgress {
    return this.detailsService.readingProgress.getValue();
  }

  get pdfUrl(): string {
    return `/pdf/${this.book.pdf}.pdf`;
  }

}

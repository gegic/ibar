import { Component, OnInit } from '@angular/core';
import {Book} from '../../core/model/book';
import {TokenService} from '../../core/services/token.service';
import {READER} from '../../core/utils/consts';
import {distinctUntilChanged} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {BookDetailsService} from '../../core/services/book-details.service';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.scss']
})
export class BookDetailsComponent implements OnInit {

  readonly navigationItems = [
    {
      label: 'About',
      link: 'about',
      icon: 'pi pi-info-circle'
    },
    {
      label: 'Reviews',
      link: 'reviews',
      icon: 'pi pi-star'
    }
  ];

  isLoading = true;

  constructor(private tokenService: TokenService,
              private activatedRoute: ActivatedRoute,
              private detailsService: BookDetailsService,
              private titleService: Title,
              private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(distinctUntilChanged()).subscribe(
      val => {
        if (!!val.id) {
          this.getBook(val.id);
        } else {
          this.router.navigate(['']);
        }
      });
  }

  getBook(id: number): void {
    this.isLoading = true;
    this.detailsService.getBook(id).pipe(distinctUntilChanged()).subscribe(
      data => {
        this.detailsService.book.next(data);
        const newTitle = `${data.name} | ibar`;
        this.titleService.setTitle(newTitle);
        this.isLoading = false;
      }
    );
  }

  onClickEdit(): void {
  }

  onClickDelete(): void {
  }

  onClickAddToReadingList(): void {
  }

  onClickRemoveFromReadingList(): void {
  }

  onClickRead(): void {
    this.router.navigate(['reading', this.book.id]);
  }

  get canRead(): boolean {
    return this.tokenService.getToken()?.authorities.some(au => au.name === READER);
  }

  get isLoggedIn(): boolean {
    return !!this.tokenService.getToken();
  }

  get reviews(): string {
    if (this.book?.numReviews === 0) {
      return 'No reviews so far.';
    } else {
      return `${(Math.round((this.book?.averageRating ?? 0) * 10) / 10).toFixed(1)} from ${this.book?.numReviews} reviews.`;
    }
  }

  get book(): Book {
    return this.detailsService.book.getValue();
  }


}

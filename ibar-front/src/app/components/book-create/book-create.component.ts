import { Component, OnInit } from '@angular/core';
import {SelectButton} from 'primeng/selectbutton';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BookService} from '../../core/services/book.service';
import {CategoryService} from '../../core/services/category.service';
import {Category} from '../../core/model/category';
import {ActivatedRoute, Router} from '@angular/router';
import {Book} from '../../core/model/book';
import {FileUpload} from 'primeng/fileupload';
import {Cover} from '../../core/model/cover';
import {MessageService} from 'primeng/api';
import {ContentFile} from '../../core/model/content-file';
import {Observable} from 'rxjs';
import {Author} from '../../core/model/author';
import {AuthorService} from '../../core/services/author.service';

interface Dictionary {
  [key: string]: string;
}

const errorDict: Dictionary = {
  name: 'Name is required and can contain only letters, digits and spaces',
  briefInfo: 'Brief info is required and it can contain a maximum of 200 characters.',
  additionalInfo: 'Additional info can contain 1000 characters at most.',
  address: 'Address is required',
  selectedCategory: 'Category is required.',
  selectedSubcategory: 'Subcategory is required'
};

@Component({
  selector: 'app-create-book',
  templateUrl: './book-create.component.html',
  styleUrls: ['./book-create.component.scss']
})
export class BookCreateComponent implements OnInit {

  mode = 'add';

  bookTypeOptions: any[] = [{label: 'eBook', value: 'E_BOOK'}, {label: 'Audio book', value: 'AUDIO_BOOK'}];
  formGroup: FormGroup = new FormGroup(
    {
      name: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      description: new FormControl(undefined, Validators.maxLength(1000)),
      bookType: new FormControl(undefined, Validators.required),
      selectedCategory: new FormControl(undefined, Validators.required),
      authors: new FormControl(undefined, Validators.required)
    }
  );

  book: Book = new Book();

  cover?: Cover;
  contentFile?: ContentFile;

  categoriesLoading = true;
  coverLoading = false;
  contentFileLoading = false;

  constructor(private bookService: BookService,
              private categoryService: CategoryService,
              private authorService: AuthorService,
              private activatedRoute: ActivatedRoute,
              private messageService: MessageService,
              private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(val => {
      if (val.mode) {
        this.mode = val.mode;
      }
    });
    this.getCategories();
    this.getAuthors();
    this.getEditMode();
  }

  saveBook(): void {
    if (!this.cover) {
      this.messageService.add(
        {
          severity: 'error',
          summary: 'Cover is missing',
          detail: 'A cover is required',
          id: 'photo-error'
        }
      );
      return;
    }
    if (!this.contentFile) {
      this.messageService.add(
        {
          severity: 'error',
          summary: 'Content is missing',
          detail: 'Content is required',
          id: 'content-error'
        }
      );
      return;
    }
    if (this.formGroup.invalid) {
      for (const c in this.formGroup.controls) {
        if (this.formGroup.controls.hasOwnProperty(c) && this.formGroup.get(c)?.invalid) {
          this.messageService.add(
            {
              severity: 'error',
              summary: 'Book wasn\'t saved',
              detail: errorDict[c],
            }
          );
          return;
        }
      }
    }

    this.book.name = this.formGroup.get('name')?.value;
    this.book.type = this.formGroup.get('bookType')?.value;
    this.book.categoryId = (this.formGroup.get('selectedCategory')?.value as Category).id;
    this.book.description = this.formGroup.get('description')?.value;
    this.book.authorIds = (this.formGroup.get('authors')?.value as Author[]).filter(a => !!a && !!a.id).map(a => a?.id ?? '') ?? [];
    this.book.cover = this.cover.path;
    this.book.pdf = this.contentFile.path;
    this.book.quantity = this.contentFile.quantity;

    if (this.mode === 'add') {
      this.bookService.addBook(this.book).subscribe(
        data => {
          this.messageService.add({
            severity: 'success',
            summary: 'Successfully added',
            detail: 'The book was added successfully',
            id: 'book-added-toast'
          });
          this.router.navigate([`/book/${data.id}`]);
        }
      );
    } else {
      this.bookService.editBook(this.book).subscribe(
        data => {
          this.messageService.add({
            severity: 'success',
            summary: 'Successfully edited',
            detail: 'The book was edited successfully',
            id: 'book-edited-toast'
          });
          this.router.navigate([`/book/${data.id}`]);
        }
      );
    }
  }

  getCategories(): void {
    this.categoriesLoading = true;
    this.categoryService.getCategories().subscribe(
      (val: Category[]) => {
        this.categoryService.categories = val;
        this.categoriesLoading = false;
      }
    );
  }

  getAuthors(): void {
    this.authorService.getAuthors().subscribe(
      (val: Author[]) => {
        this.authorService.authors = val;
      }
    );
  }

  getEditMode(): void {
    if (this.mode === 'edit') {
      this.activatedRoute.params.subscribe(val => {
        if (val.id) {
          this.getBook(val.id);
        }
      });
    }
  }

  getBook(id: string): void {
    this.bookService.getBook(id).subscribe((book: Book) => {
      const bookAuthors = this.authors.filter(au => book.authorIds?.includes(au?.id ?? ''));
      this.book = book;
      this.formGroup.patchValue({
        name: book.name,
        description: book.description,
        bookType: book.type,
        selectedCategory: {id: book.categoryId, name: book.categoryName},
        authors: bookAuthors
      });
      this.contentFile = new ContentFile(book.pdf, book.quantity);
      this.cover = new Cover(book.cover);
    });
  }

  coverChosen(event: any): void {
    const file = event.target.files[0];
    this.coverLoading = true;
    this.bookService.addCover(file).subscribe(
      (data: Cover) => {
        this.coverLoading = false;
        this.cover = data;
      },
      () => {
        this.coverLoading = false;
        this.messageService.add({severity: 'error', detail: 'Photo couldn\'t be uploaded due to an unknown reason.'});
      }
    );
  }

  pdfChosen(event: any): void {
    const file = event.files[0];
    const observable = this.bookService.addPdf(file);
    this.contentChosen(file, observable);
  }

  audioChosen(event: any): void {
    const file = event.files[0];
    const observable = this.bookService.addAudio(file);
    this.contentChosen(file, observable);
  }

  contentChosen(file: File, observable: Observable<ContentFile>): void {
    this.contentFileLoading = true;
    observable.subscribe(
      (data: ContentFile) => {
        this.contentFileLoading = false;
        this.contentFile = data;
      },
      () => {
        this.contentFileLoading = false;
        this.messageService.add({severity: 'error', detail: 'Content couldn\'t be uploaded due to an unknown reason.'});
      }
    );
  }

  get authors(): Author[] {
    return this.authorService.authors;
  }

  get categories(): Category[] {
    return this.categoryService.categories;
  }

  get coverPath(): string {
    return `/covers/${this.cover?.path}.png`;
  }
}

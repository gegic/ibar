import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

import { MessageService } from 'primeng/api';

import { Author } from 'src/app/core/model/author';

import { AuthorService } from 'src/app/core/services/author.service';

@Component({
  selector: 'app-author',
  templateUrl: './author-list.component.html',
  styleUrls: ['./author-list.component.scss']
})
export class AuthorListComponent implements OnInit {

  isAddDialogOpen = false;
  authorForm: FormGroup;
  editingAuthor?: Author;
  image?: SafeUrl;

  constructor(
    private formBuilder: FormBuilder,
    private authorService: AuthorService,
    private messageService: MessageService,
    private domSanitizer: DomSanitizer) {
    this.authorForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      dateOfBirth: [new Date(), [Validators.required]],
      dateOfDeath: [new Date(), []],
      image: [null, []],
    });

  }

  ngOnInit(): void {
    this.resetAuthors();
  }

  resetAuthors(): void {
    this.authorService.authors = [];

    this.authorForm.reset();

    this.getAuthors();
  }

  getAuthors(): void {
    this.authorService.getAuthors().subscribe(
      val => {
        for (const el of val) {
          if (this.authorService.authors.some(mod => mod.id === el.id)) {
            continue;
          }
          this.authorService.authors.push(el);
        }
      }
    );
  }

  openAddDialog(editing = false, author?: Author): void {
    this.isAddDialogOpen = true;

    if (editing) {
      this.authorForm.patchValue({
        name: author?.name ?? '',
        description: author?.description ?? '',
        dateOfBirth: new Date(author?.dateOfBirth ?? ''),
        dateOfDeath: new Date(author?.dateOfDeath ?? '')
      });

      this.editingAuthor = author;
    }
  }

  onHideAddDialog(): void {
    this.authorForm.reset();
    this.editingAuthor = undefined;
    this.image = undefined;
  }

  saveAuthor(): void {
    if (!this.authorForm.valid) {
      this.messageService.add(
        { id: 'toast-container', severity: 'error', summary: 'Required', detail: 'Please, fill in all required fields.' }
      );

      return;
    }


    let author: Author;

    if (!!this.editingAuthor) {
      author = this.editingAuthor;
    }
    else {
      author = new Author();
    }

    author.name = this.authorForm.controls.name.value;
    author.description = this.authorForm.controls.description.value;
    author.dateOfBirth = this.authorForm.controls.dateOfBirth.value;
    author.dateOfDeath = this.authorForm.controls.dateOfDeath.value;

    const formData = new FormData();

    const blob = new Blob([JSON.stringify(author)], {
      type: 'application/json'
    });

    formData.append('authorDTO', blob);
    formData.append('file', this.authorForm.controls.image.value ?? new File([], ''));

    if (!!this.editingAuthor) {
      this.authorService.update(this.editingAuthor?.id ?? '', formData).subscribe(res => {
        this.updateListOfAuthors(res);

        this.showSuccessMessageOnUpdateOrCreateAuthor('Update');
      },
        err => {
          this.showErrorMessageOnUpdateOrCreateAuthor('Update');
        });
    }
    else {
      this.authorService.create(formData).subscribe(res => {
        this.updateListOfAuthors(res);

        this.showSuccessMessageOnUpdateOrCreateAuthor('Create');
      },
        err => {
          this.showErrorMessageOnUpdateOrCreateAuthor('Create');
        });
    }
  }

  authorDeletionConfirmed(): void {
    this.resetAuthors();
  }

  uploadFile(e: any, form: any): void {
    const imageURL = URL.createObjectURL(e.files[0]);

    this.image = this.domSanitizer.bypassSecurityTrustUrl(imageURL);

    this.authorForm.patchValue({
      image: e.files[0]
    });

    form.clear();
  }
  get authors(): Author[] {
    return this.authorService.authors;
  }

  get editing(): boolean {
    return !!this.editingAuthor;
  }

  private updateListOfAuthors(author: Author): void {
    const index: number = this.authors.findIndex(auth => auth.id === author.id);

    if (index !== -1) {
      this.authors[index] = author;
    }
    else {
      this.authors.push(author);
    }
  }

  private showErrorMessageOnUpdateOrCreateAuthor(operation: string): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: `${operation} unsuccessful`,
      detail: 'Error encountered. Please try again.'
    });
  }

  private showSuccessMessageOnUpdateOrCreateAuthor(operation: string): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: `${operation} successfully`,
      detail: `The author was ${operation.toLocaleLowerCase()}d successfully`
    });
  }
}

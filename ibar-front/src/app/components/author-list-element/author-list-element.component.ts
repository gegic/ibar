import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ConfirmationService, MessageService} from 'primeng/api';
import {Author} from 'src/app/core/model/author';
import {AuthorService} from 'src/app/core/services/author.service';

@Component({
  selector: 'app-author-list',
  templateUrl: './author-list-element.component.html',
  styleUrls: ['./author-list-element.component.scss']
})
export class AuthorListElementComponent implements OnInit {

  @Input()
  author!: Author;

  @Output()
  authorDeleted: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  clickEdit: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private authorService: AuthorService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  public ngOnInit(): void { }

  public onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.author.name ?? ''}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  public onClickEdit(): void {
    this.clickEdit.emit(this.author);
  }

  private deletionConfirmed(): void {
    this.authorService.delete(this.author?.id ?? '').subscribe((res) => {
      if (res) {
        this.showToastWhenDeleteAuthorSucceed();
      }
      else {
        this.showToastWhenDeleteAuthorFailed();
      }
    });
  }

  private showToastWhenDeleteAuthorSucceed(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: 'Deleted successfully',
      detail: 'The author was deleted successfully'
    });
    this.authorDeleted.emit(this.author);
  }

  private showToastWhenDeleteAuthorFailed(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: 'Deletion unsuccessful',
      detail: 'There are books written by this author. Plase, first remove them and then try again.'
    });
  }

}

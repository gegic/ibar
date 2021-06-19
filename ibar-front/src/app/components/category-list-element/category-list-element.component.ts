import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

import {ConfirmationService, MessageService} from 'primeng/api';

import {Category} from 'src/app/core/model/category';

import {CategoryService} from 'src/app/core/services/category.service';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list-element.component.html',
  styleUrls: ['./category-list-element.component.scss']
})
export class CategoryListElementComponent implements OnInit {

  @Input()
  category!: Category;

  @Output()
  categoryDeleted: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  clickEdit: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private categoryService: CategoryService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  public ngOnInit(): void { }

  public onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.category.name ?? ''}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  public onClickEdit(): void {
    this.clickEdit.emit(this.category);
  }

  private deletionConfirmed(): void {
    this.categoryService.delete(this.category?.id ?? '').subscribe((res) => {
      if (res) {
        this.showToastWhenDeleteCategorySucceed();
      }
      else {
        this.showToastWhenDeleteCategoryFailed();
      }
    });
  }

  private showToastWhenDeleteCategorySucceed(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: 'Deleted successfully',
      detail: 'The category was deleted successfully'
    });
    this.categoryDeleted.emit(this.category);
  }

  private showToastWhenDeleteCategoryFailed(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: 'Deletion unsuccessful',
      detail: 'There are books or plans that contain this category. Plase, first remove them and then try again.'
    });
  }

}

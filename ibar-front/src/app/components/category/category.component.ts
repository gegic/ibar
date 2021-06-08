import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

import { MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';

import { Category } from 'src/app/core/model/category';

import { CategoryService } from 'src/app/core/services/category.service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {


  isAddDialogOpen = false;
  nameControl = new FormControl('', [Validators.required]);
  editingCategory?: Category;

  constructor(
    private categoryService: CategoryService,
    private messageService: MessageService,
    private dialogService: DialogService) { }

  ngOnInit(): void {
    this.resetCategories();
  }

  resetCategories(): void {
    this.categoryService.categories = [];

    this.nameControl.reset();

    this.getCategories();
  }

  getCategories(): void {
    this.categoryService.getCategories().subscribe(
      val => {
        for (const el of val) {
          if (this.categoryService.categories.some(mod => mod.id === el.id)) {
            continue;
          }
          this.categoryService.categories.push(el);
        }
      }
    );
  };

  openAddDialog(editing = false, category?: Category): void {
    this.isAddDialogOpen = true;

    if (editing) {
      this.nameControl.setValue(category?.name ?? '');

      this.editingCategory = category;
    }
  };

  onHideAddDialog(): void {
    this.nameControl.reset();
    this.editingCategory = undefined;
  };

  saveCategory(): void {
    if (!this.nameControl.valid) {
      this.messageService.add(
        { id: 'toast-container', severity: 'error', summary: 'Required', detail: 'Name is required.' }
      );
    }

    const name = this.nameControl.value;

    let c: Category;

    if (!!this.editingCategory) {
      c = this.editingCategory;
    }
    else {
      c = new Category();
    }

    c.name = name;

    if (!!this.editingCategory) {
      this.categoryService.update(this.editingCategory.id, c).subscribe(res => {
        this.updateListOfCategories(res);

        this.showSuccessMessageOnUpdateOrCreateCategory("Update");
      },
        err => {
          this.showErrorMessageOnUpdateOrCreateCategory("Update");
        });
    }
    else {
      this.categoryService.create(c).subscribe(res => {
        this.updateListOfCategories(res);

        this.showSuccessMessageOnUpdateOrCreateCategory("Create");
      },
        err => {
          this.showErrorMessageOnUpdateOrCreateCategory("Create");
        });
    }
  };

  categoryDeletionConfirmed(): void {
    this.resetCategories();
  };

  get categories(): Category[] {
    return this.categoryService.categories;
  };

  get editing(): boolean {
    return !!this.editingCategory;
  };

  private updateListOfCategories(category: Category) {
    let index: number = this.categories.findIndex(cat => cat.id === category.id);

    if (index != -1) {
      this.categories[index] = category;
    }
    else {
      this.categories.push(category);
    }
  }

  private showErrorMessageOnUpdateOrCreateCategory(operation: string) {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: `${operation} unsuccessful`,
      detail: 'Category name is already in use, please use a different category name.'
    });
  }

  private showSuccessMessageOnUpdateOrCreateCategory(operation: string) {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: `${operation} successfully`,
      detail: `The category was ${operation.toLocaleLowerCase()}d successfully`
    });
  }

}

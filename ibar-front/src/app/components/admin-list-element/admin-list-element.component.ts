import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { User } from 'src/app/core/model/user';
import { AdminService } from 'src/app/core/services/admin.service';

@Component({
  selector: 'app-admin-list',
  templateUrl: './admin-list-element.component.html',
  styleUrls: ['./admin-list-element.component.scss']
})
export class AdminListElementComponent implements OnInit {

  @Input()
  admin!: User;

  @Output()
  adminDeleted: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  clickEdit: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private adminService: AdminService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  public ngOnInit(): void { }

  public onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.admin.firstName} ${this.admin.lastName}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  public onClickEdit(): void {
    this.clickEdit.emit(this.admin);
  }

  private deletionConfirmed(): void {
    this.adminService.delete(this.admin?.id ?? '').subscribe((res) => {
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
      detail: 'The admin was deleted successfully'
    });
    this.adminDeleted.emit(this.admin);
  }

  private showToastWhenDeleteCategoryFailed(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: 'Deletion unsuccessful',
      detail: 'Error encountered. Please try again.'
    });
  }

}

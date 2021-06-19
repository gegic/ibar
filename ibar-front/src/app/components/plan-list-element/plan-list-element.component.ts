import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Plan } from 'src/app/core/model/plan';
import { PlanService } from 'src/app/core/services/plan.service';

@Component({
  selector: 'app-plan-list',
  templateUrl: './plan-list-element.component.html',
  styleUrls: ['./plan-list-element.component.scss']
})
export class PlanListElementComponent implements OnInit {

  @Input()
  plan!: Plan;

  @Output()
  planDeleted: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  clickEdit: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private planService: PlanService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  public ngOnInit(): void { }

  public onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.plan.name ?? ''} plan?`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  public onClickEdit(): void {
    this.clickEdit.emit(this.plan);
  }

  private deletionConfirmed(): void {
    this.planService.delete(this.plan?.id ?? '').subscribe((res) => {
      if (res) {
        this.showToastWhenDeletePlanSucceed();
      }
      else {
        this.showToastWhenDeletePlanFailed();
      }
    });
  }

  private showToastWhenDeletePlanSucceed(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: 'Deleted successfully',
      detail: 'The plan was deleted successfully'
    });
    this.planDeleted.emit(this.plan);
  }

  private showToastWhenDeletePlanFailed(): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: 'Deletion unsuccessful',
      detail: 'There are users subscribed on this plan. Please, try gain.'
    });
  }

}

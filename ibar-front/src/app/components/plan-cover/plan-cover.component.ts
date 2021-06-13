import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Plan} from '../../core/model/plan';
import {ConfirmationService} from 'primeng/api';
import {NavbarService} from '../../core/services/navbar.service';

@Component({
  selector: 'app-plan-cover',
  templateUrl: './plan-cover.component.html',
  styleUrls: ['./plan-cover.component.scss']
})
export class PlanCoverComponent implements OnInit {

  @Input()
  plan: Plan = new Plan();
  @Output()
  purchaseConfirmed: EventEmitter<Plan> = new EventEmitter<Plan>();

  constructor(private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
  }

  confirmPlan(): void {
    this.confirmationService.confirm(
      {
        header: 'Confirm this purchase',
        message: 'Let\'s imagine the entire payment process here',
        accept: () => this.purchaseConfirmed.emit(this.plan)
      });
  }

}

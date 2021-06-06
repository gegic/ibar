import { Component, OnInit } from '@angular/core';
import {RESPONSIVE_OPTIONS} from '../../core/utils/consts';
import {Plan} from '../../core/model/plan';
import {PlanService} from '../../core/services/plan.service';
import {Router} from '@angular/router';
import {Message, MessageService} from 'primeng/api';

@Component({
  selector: 'app-purchase-plan',
  templateUrl: './purchase-plan.component.html',
  styleUrls: ['./purchase-plan.component.scss']
})
export class PurchasePlanComponent implements OnInit {

  loadingPlans = true;
  plans: Plan[] = [];
  responsiveOptions = RESPONSIVE_OPTIONS;

  constructor(private planService: PlanService,
              private messageService: MessageService,
              private router: Router) {}

  ngOnInit(): void {
    this.getPlans();
  }

  getPlans(): void {
    this.loadingPlans = true;
    this.planService.getPlans().subscribe((plans: Plan[]) => {
      this.plans = plans;
      this.loadingPlans = false;
    });
  }

  purchaseConfirmed(plan: Plan): void {
    this.planService.doSubscription(plan).subscribe(
      () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: `${plan.name} was purchased sucessfully`
      });
        this.router.navigate(['']);
      }
    );
  }

}

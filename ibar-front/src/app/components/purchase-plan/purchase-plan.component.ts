import {Component, OnDestroy, OnInit} from '@angular/core';
import {READER_NAVBAR, RESPONSIVE_OPTIONS} from '../../core/utils/consts';
import {Plan} from '../../core/model/plan';
import {PlanService} from '../../core/services/plan.service';
import {Router} from '@angular/router';
import {Message, MessageService} from 'primeng/api';
import {NavbarService} from '../../core/services/navbar.service';

@Component({
  selector: 'app-purchase-plan',
  templateUrl: './purchase-plan.component.html',
  styleUrls: ['./purchase-plan.component.scss']
})
export class PurchasePlanComponent implements OnInit, OnDestroy {

  loadingPlans = true;
  plans: Plan[] = [];
  responsiveOptions = RESPONSIVE_OPTIONS;

  constructor(private planService: PlanService,
              private messageService: MessageService,
              private router: Router,
              private navbarService: NavbarService) {}

  ngOnInit(): void {
    this.getPlans();
    this.navbarService.navigation.next(null);
    this.navbarService.hasSearch.next(false);
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
      },
      () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Rank too low',
          detail: `This plan can be purchased only by readers with rank ${plan.rankName} or higher`
        });
      }
    );
  }

  ngOnDestroy(): void {
    this.navbarService.navigation.next(READER_NAVBAR);
    this.navbarService.hasSearch.next(true);
  }

}

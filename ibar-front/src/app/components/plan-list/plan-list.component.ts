import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Category } from 'src/app/core/model/category';
import { Plan } from 'src/app/core/model/plan';
import { Rank } from 'src/app/core/model/rank';
import { CategoryService } from 'src/app/core/services/category.service';
import { PlanService } from 'src/app/core/services/plan.service';
import { RankService } from 'src/app/core/services/rank.service';

@Component({
  selector: 'app-plan',
  templateUrl: './plan-list.component.html',
  styleUrls: ['./plan-list.component.scss']
})
export class PlanListComponent implements OnInit {

  isAddDialogOpen = false;
  planForm: FormGroup;
  editingPlan?: Plan;

  constructor(
    private formBuilder: FormBuilder,
    private planService: PlanService,
    private categoryService: CategoryService,
    private rankService: RankService,
    private messageService: MessageService,
  ) {
    this.planForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      price: ['', [Validators.required, Validators.min(1)]],
      categories: [[], [Validators.required, Validators.minLength(1)]],
      rank: ['', [Validators.required]],
      durationInDays: [0, [Validators.required, Validators.min(1)]],
      description: ['']
    });

    this.getCategories();

    this.getRanks();
  }

  ngOnInit(): void {
    this.resetPlans();
  }

  getCategories(): void {
    this.categoryService.getCategories().subscribe(
      (val: Category[]) => {
        this.categoryService.categories = val;
      }
    );
  }

  getRanks(): void {
    this.rankService.getRanks().subscribe(
      (val: Rank[]) => {
        this.rankService.ranks = val;
      }
    );
  }

  resetPlans(): void {
    this.planService.plans = [];

    this.planForm.reset();

    this.getPlans();
  }

  getPlans(): void {
    this.planService.getPlans().subscribe(
      val => {
        for (const el of val) {
          if (this.planService.plans.some(mod => mod.id === el.id)) {
            continue;
          }
          this.planService.plans.push(el);
        }
      }
    );
  }

  openAddDialog(editing = false, plan?: Plan): void {
    this.isAddDialogOpen = true;

    if (editing) {
      this.planForm.patchValue({
        name: plan?.name,
        price: plan?.price,
        categories: this.createCategoryObjects(plan ?? new Plan()),
        rank: this.createRankObject(plan?.rankId ?? ''),
        durationInDays: plan?.dayDuration,
        description: plan?.description
      });

      this.editingPlan = plan;
    }
  }

  onHideAddDialog(): void {
    this.planForm.reset();
    this.editingPlan = undefined;
  }

  savePlan(): void {
    if (!this.planForm.valid) {
      this.messageService.add(
        { id: 'toast-container', severity: 'error', summary: 'Required', detail: 'Please, fill in all required fields.' }
      );

      return;
    }


    let plan: Plan;

    if (!!this.editingPlan) {
      plan = this.editingPlan;
    }
    else {
      plan = new Plan();
    }

    plan.name = this.planForm.controls.name.value;
    plan.price = this.planForm.controls.price.value;
    plan.categoryIds = this.getIdsFromListOfCategoryObjects(this.planForm.controls.categories.value);
    plan.rankId = this.getIdFromRankObject(this.planForm.controls.rank.value);
    plan.dayDuration = this.planForm.controls.durationInDays.value;
    plan.description = this.planForm.controls.description.value;

    if (!!this.editingPlan) {
      this.planService.update(this.editingPlan?.id ?? '', plan).subscribe(res => {
        this.updateListOfPlans(res);

        this.showSuccessMessageOnUpdateOrCreatePlan('Update');
      },
        err => {
          this.showErrorMessageOnUpdateOrCreatePlan('Update');
        });
    }
    else {
      this.planService.create(plan).subscribe(res => {
        this.updateListOfPlans(res);

        this.showSuccessMessageOnUpdateOrCreatePlan('Create');
      },
        err => {
          this.showErrorMessageOnUpdateOrCreatePlan('Create');
        });
    }
  }

  planDeletionConfirmed(): void {
    this.resetPlans();
  }

  get plans(): Plan[] {
    return this.planService.plans;
  }

  get categories(): Category[] {
    return this.categoryService.categories;
  }

  get ranks(): Rank[] {
    return this.rankService.ranks;
  }

  get editing(): boolean {
    return !!this.editingPlan;
  }

  private updateListOfPlans(newPlan: Plan): void {
    const index: number = this.plans.findIndex(plan => plan.id === newPlan.id);

    if (index !== -1) {
      this.plans[index] = newPlan;
    }
    else {
      this.plans.push(newPlan);
    }
  }

  private showErrorMessageOnUpdateOrCreatePlan(operation: string): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'error',
      summary: `${operation} unsuccessful`,
      detail: 'Error encountered. Please try again.'
    });
  }

  private showSuccessMessageOnUpdateOrCreatePlan(operation: string): void {
    this.messageService.add({
      id: 'toast-container',
      severity: 'success',
      summary: `${operation} successfully`,
      detail: `The plan was ${operation.toLocaleLowerCase()}d successfully`
    });
  }

  private createCategoryObjects(plan: Plan): Category[] {
    const categories: Category[] = [];

    for (const category of this.categories) {
      if (this.isCategoryIdInPlanCategories(category.id ?? '', plan.categoryIds ?? [])) {
        categories.push(category);
      }
    }

    return categories;
  }

  private isCategoryIdInPlanCategories(categoryId: string, categories: string[]): boolean {
    const index: number = categories.findIndex(category => category === categoryId);

    return index != -1;
  }

  private createRankObject(rankId: string): Rank {
    const index: number = this.ranks.findIndex(rank => rank.id === rankId);

    return index != -1 ? this.ranks[index] : new Rank();
  }

  private getIdFromRankObject(rank: Rank): string {
    return rank.id ?? '';
  }

  private getIdsFromListOfCategoryObjects(categories: Category[]): string[] {
    return categories.map(this.getIdFromCategoryObject);
  }

  private getIdFromCategoryObject(category: Category): string {
    return category.id ?? '';
  }
}

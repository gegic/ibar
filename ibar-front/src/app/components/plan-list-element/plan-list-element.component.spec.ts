import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanListElementComponent } from './plan-list-element.component';

describe('PlanListElementComponent', () => {
  let component: PlanListElementComponent;
  let fixture: ComponentFixture<PlanListElementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlanListElementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlanListElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

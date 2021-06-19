import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Plan } from '../model/plan';

@Injectable({
  providedIn: 'root'
})
export class PlanService {

  plans: Plan[] = [];

  PLAN_API = 'api/plans';

  constructor(private httpClient: HttpClient) { }

  getPlans(): Observable<Plan[]> {
    return this.httpClient.get<Plan[]>(`${this.PLAN_API}`);
  }

  doSubscription(plan: Plan): Observable<void> {
    return this.httpClient.post<void>(`${this.PLAN_API}/subscribe`, plan);
  }

  public delete(id: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(`${this.PLAN_API}/${id}`);
  }

  public create(data: Plan): Observable<Plan> {
    return this.httpClient.post<Plan>(`${this.PLAN_API}`, data);
  }

  public update(id: string, data: Plan): Observable<Plan> {
    return this.httpClient.put<Plan>(`${this.PLAN_API}/${id}`, data);
  }
}

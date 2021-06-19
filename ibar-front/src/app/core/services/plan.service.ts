import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Plan} from '../model/plan';

@Injectable({
  providedIn: 'root'
})
export class PlanService {

  PLAN_API = 'api/plans';

  constructor(private httpClient: HttpClient) { }

  getPlans(): Observable<Plan[]> {
    return this.httpClient.get<Plan[]>(`${this.PLAN_API}`);
  }

  doSubscription(plan: Plan): Observable<void> {
    return this.httpClient.post<void>(`${this.PLAN_API}/subscribe`, plan);
  }
}

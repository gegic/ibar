import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Rank} from '../model/rank';

@Injectable({
  providedIn: 'root'
})
export class RankService {

  private readonly API_URL = '/api/ranks';

  constructor(private httpClient: HttpClient) { }

  getRanks(): Observable<Rank[]> {
    return this.httpClient.get<Rank[]>(`${this.API_URL}`);
  }

  create(rank: Rank): Observable<Rank> {
    return this.httpClient.post<Rank>(`${this.API_URL}`, rank);
  }

  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.API_URL}`);
  }

  update(rank: Rank): Observable<Rank> {
    return this.httpClient.put<Rank>(`${this.API_URL}`, rank);
  }

}

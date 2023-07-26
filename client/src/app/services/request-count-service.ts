import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RequestCountService {
  private apiServerUrl: string;

  constructor(private http: HttpClient) {
    this.apiServerUrl = 'http://localhost:8080';
  }

  public getRequestCount(): Observable<Number> {
    return this.http.get<Number>(`${this.apiServerUrl}/requestCount`);
  }
}

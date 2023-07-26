import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Task } from '../model/task';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiServerUrl: string;

  constructor(private http: HttpClient) {
    this.apiServerUrl = 'http://localhost:8080';
  }

  public findAll(sortBy?: string): Observable<Task[]> {
    const url = sortBy ? `${this.apiServerUrl}/task/list?sortBy=${sortBy}` : `${this.apiServerUrl}/task/list`;
    return this.http.get<Task[]>(url);
  }

  public add(task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.apiServerUrl}/task/`, task);
  }

  public update(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiServerUrl}/task/`, task);
  }

  public deleteById(taskId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/task/${taskId}`);
  }
}

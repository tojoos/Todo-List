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

  public findAll(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiServerUrl}/task/list`);
  }

  public add(task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.apiServerUrl}/task/add`, task);
  }

  public update(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiServerUrl}/task/update`, task);
  }

  public deleteById(taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/task/${taskId}`);
  }
}

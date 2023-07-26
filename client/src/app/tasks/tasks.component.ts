import { Component, OnInit } from '@angular/core';
import { Task } from '../model/task';
import { TaskService } from '../services/task-service';
import { HttpErrorResponse } from '@angular/common/http';
import { RequestCountService } from '../services/request-count-service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  public tasks: Task[] = [];
  public allTasks: Task[] = [];
  public editTask!: Task | undefined;
  public deleteTask!: Task | undefined;
  public currentRequestCount: Number = 0;
  public activeSortButton: number = 0;

  constructor(
    private taskService: TaskService,
    private requestCountService: RequestCountService) { }

  ngOnInit() {
    this.getTasks();
    this.getRequestCount();
  }

  public getRequestCount(): void {
    this.requestCountService.getRequestCount().subscribe({
      next: (response: Number) => {
        this.currentRequestCount = response;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  public getTasks(field? : string): void {
    this.taskService.findAll(field).subscribe({
      next: (response: Task[]) => {
        this.tasks = response;
        this.allTasks = this.tasks;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
    this.getRequestCount();
  }

  public onAddTask(addForm: NgForm): void {
    document.getElementById('add-form')!.click();
    this.taskService.add(addForm.value).subscribe({
      next: (response: Task) => {
        console.log(response);
        this.getTasks();
        addForm.reset();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
        addForm.reset();
      }
    });
  }

  public onUpdateTask(task: Task): void {
    document.getElementById('edit-form')!.click();
    this.taskService.update(task).subscribe({
      next: (response: Task) => {
        console.log(response);
        this.getTasks();
        },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  public onDeleteTask(taskId: string): void {
    this.taskService.deleteById(taskId).subscribe({
      next: (response: void) => {
        console.log(response);
        this.getTasks();
        },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  public searchTasks(key: string): void {
    const results: Task[] = [];
    this.tasks = this.allTasks;
    for (const task of this.tasks) {
      if (task.description.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || task.priority.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || task.title.toLowerCase().indexOf(key.toLowerCase()) !== -1
      ) {
        results.push(task);
      }
    }
    this.tasks = results;
    if (!key) {
      this.tasks = this.allTasks;
    }
  }

  public sortBy(field: string, buttonNumber: number): void {
    this.activeSortButton = buttonNumber;
    this.getTasks(field);
  }

  public onOpenModal(mode: string, task?: Task): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'add') {
      button.setAttribute('data-target', '#addModal');
    } else if (mode === 'edit') {
      this.editTask = task!;
      button.setAttribute('data-target', '#editModal');
    } else if (mode === 'delete') {
      this.deleteTask = task!;
      button.setAttribute('data-target', '#deleteModal');
    }
    container!.appendChild(button);
    button.click();
  }
}

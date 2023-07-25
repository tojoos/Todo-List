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
  public currentRequestCount: Number = 0;

  constructor(
    private taskService: TaskService,
    private requestCountService: RequestCountService) { }

  ngOnInit() {
    this.getTasks();
    this.getRequestCount();
  }

  public getTasks(): void {
    this.taskService.findAll().subscribe({
      next: (response: Task[]) => {
        this.tasks = response;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  //add add/update/delte handlers - use form maybe todo it as such in announcement components

  //add task - formularz
  public onAddAnnouncement(addForm: NgForm): void {
    document.getElementById('add-announcement-form')!.click();

    console.log(addForm.value)

    this.announcementService.addAnnouncement(addForm.value).subscribe({
      next: (response: Announcement) => {
        console.log(response);
        this.getAnnouncements();
        addForm.reset();
        },
      error: (error: HttpErrorResponse) => {
        if (error.status === 403) {
          alert("Couldn't create an announcement. Make sure you are logged in.")
        } else {
          alert(error)
        }
        addForm.reset();
      }
    });
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

  public getAnnouncements(): void {
    this.announcementService.getAnnouncements().subscribe({
      next: (response: Announcement[]) => {
        this.announcements = response;
        this.announcements.sort((f1, f2) => new Date(f2.creationDateTime).getTime() - new Date(f1.creationDateTime).getTime());
        this.announcements.sort((f1, f2) => f2.status === 'closed' ? -1 : 1)
        this.latestAnnouncement = this.announcements[0]
        this.announcements.splice(0,1)
        },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  public onOpenModal(mode: string, announcement?: Announcement): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#addAnnouncementModal');
    container!.appendChild(button);
    button.click();
  }
}

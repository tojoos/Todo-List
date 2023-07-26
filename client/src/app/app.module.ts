import { NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';

import { TasksComponent } from './tasks/tasks.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    TasksComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [TasksComponent]
})
export class AppModule {
  constructor(private titleService: Title) {
    this.titleService.setTitle("Todo List");
   }
}

import { Time } from "@angular/common";

export interface Task {
  id: string,
  title: string,
  description: string,
  priority: string,
  creationTimeStamp: Date
}

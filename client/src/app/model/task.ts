import { Time } from "@angular/common";

export interface Task {
  id: string,
  description: string,
  priority: string,
  creationTimeStamp: Time
}

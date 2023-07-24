package tojoos.todolist.service;

import tojoos.todolist.pojo.Task;
import java.util.List;

public interface TaskService {
  Task findById(String uuid);
  List<Task> findAll(String sortedBy);
  Task add(Task task);
  void deleteById(String uuid);
  Task update(Task task);
}

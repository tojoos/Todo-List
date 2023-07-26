package tojoos.todolist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tojoos.todolist.exception.InvalidUUIDException;
import tojoos.todolist.exception.TaskNotFoundException;
import tojoos.todolist.pojo.Task;
import tojoos.todolist.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  public TaskServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Task findById(String uuid) {
    UUID validId;
    try {
      validId = UUID.fromString(uuid);
    } catch (IllegalArgumentException ex) {
      throw new InvalidUUIDException("Provided value of id = \"" + uuid + "\" is invalid.");
    }
    return taskRepository.findById(validId)
        .orElseThrow(() -> new TaskNotFoundException("Task with id: \"" + uuid +
            "\" couldn't be found."));
  }

  public List<Task> findAll(String sortBy) {
    // Use the findAll method with Sort to get the sorted entities by input argument, if not specified return unsorted
    if (sortBy != null) {
      if (sortBy.equals("creationTimeStamp") || sortBy.equals("title") | sortBy.equals("description")) {
        return taskRepository.findAll(Sort.by(Sort.Order.by(sortBy)));
      } else if (sortBy.equals("priority")) {
        return getTasksSortedByPriority();
      } else {
        throw new IllegalArgumentException("Task entities cannot be sorted by provided field: " + sortBy);
      }
    } else {
      return taskRepository.findAll();
    }
  }

  public Task add(Task task) {
    task.setCreationTimeStamp(LocalDateTime.now());
    Task savedTask = taskRepository.save(task);
    log.info("Added new task with id = \"" + savedTask.getId() + "\".");
    return savedTask;
  }

  public void deleteById(String uuid) {
    //Check if task exists
    Task foundTask = this.findById(uuid);
    taskRepository.delete(foundTask);
    log.info("Successfully deleted task with id = \"" + uuid + "\".");
  }

  public Task update(Task task) {
    //Check if task with uuid exists
    this.findById(String.valueOf(task.getId()));
    //update entity
    log.info("Updating task with id = \"" + task.getId() + "\".");
    return taskRepository.save(task);
  }

  private List<Task> getTasksSortedByPriority() {
    List<Task> foundTasks = new ArrayList<>(taskRepository.findAll());
    //sort by enum value not literal
    foundTasks.sort(Comparator.comparingInt(task -> task.getPriority().value));
    // revert to return HIGH priority tasks first
    Collections.reverse(foundTasks);
    return foundTasks;
  }
}

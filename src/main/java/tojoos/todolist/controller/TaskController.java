package tojoos.todolist.controller;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tojoos.todolist.pojo.Task;
import tojoos.todolist.service.TaskService;
import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin("*")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> findById(@PathVariable String id) {
    Task foundTask = taskService.findById(id);
    return new ResponseEntity<>(foundTask, HttpStatus.OK);
  }

  @GetMapping("/list")
  public ResponseEntity<List<Task>> findAll(@RequestParam(required = false) @DefaultValue("creationTimeStamp") String sortBy) {
    List<Task> foundTasks = taskService.findAll(sortBy);
    return new ResponseEntity<>(foundTasks, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<Task> add(@RequestBody Task task) {
    Task addedTask = taskService.add(task);
    return new ResponseEntity<>(addedTask, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable String id) {
    //if task doesn't exist, it will throw exception
    taskService.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/")
  public ResponseEntity<Task> update(@RequestBody Task task) {
    Task updatedTask = taskService.update(task);
    return new ResponseEntity<>(updatedTask, HttpStatus.OK);
  }
}

package tojoos.todolist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import tojoos.todolist.exception.InvalidUUIDException;
import tojoos.todolist.exception.TaskNotFoundException;
import tojoos.todolist.pojo.Priority;
import tojoos.todolist.pojo.Task;
import tojoos.todolist.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

  @Mock
  TaskRepository taskRepository;

  @InjectMocks
  TaskServiceImpl taskServiceImpl;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void findById() {
    Task task1 = Task.builder().description("Some task.").build();

    //when
    when(taskRepository.findById(any())).thenReturn(Optional.ofNullable(task1));

    //then
    Task taskFound = taskServiceImpl.findById(String.valueOf(UUID.randomUUID()));

    assertNotNull(taskFound);
    assertDoesNotThrow(() -> taskServiceImpl.findById(String.valueOf(UUID.randomUUID())));
    verify(taskRepository, times(2)).findById(any());
  }

  @Test
  void findByIdInvalidId() {
    String invalidUUID = "Invalid UUID";

    //then
    assertThrows(
        InvalidUUIDException.class,
        () -> taskServiceImpl.findById(invalidUUID),
        "Provided value of id = \"" + invalidUUID + "\" is invalid.");

    verify(taskRepository, times(0)).findById(any());
  }

  @Test
  void findByIdNotFound() {
    String wrongUUID = String.valueOf(UUID.randomUUID());

    //when
    when(taskRepository.findById(any())).thenReturn(Optional.empty());

    //then
    assertThrows(
        TaskNotFoundException.class,
        () -> taskServiceImpl.findById(wrongUUID),
        "Task with id: \"" + wrongUUID +
            "\" couldn't be found.");

    verify(taskRepository, times(1)).findById(any());
  }

  @Test
  void findAllDefault() {
    Task task1 = Task.builder().description("Some task.").build();
    Task task2 = Task.builder().description("Some task.").build();
    Task task3 = Task.builder().description("Some task.").build();

    List<Task> tasks = List.of(task1, task2, task3);

    //when
    when(taskRepository.findAll()).thenReturn(tasks);

    //then
    List<Task> tasksFound = taskServiceImpl.findAll(null);

    assertNotNull(tasksFound);
    assertEquals(tasks.size(), tasksFound.size());
    verify(taskRepository, times(1)).findAll();
  }

  @Test
  void findAllByPriority() {
    Task task1 = Task.builder().priority(Priority.MEDIUM).build();
    Task task2 = Task.builder().priority(Priority.HIGH).build();
    Task task3 = Task.builder().priority(Priority.LOW).build();
    Task task4 = Task.builder().priority(Priority.HIGH).build();

    List<Task> tasks = List.of(task1, task2, task3, task4);

    //when
    when(taskRepository.findAll()).thenReturn(tasks);

    //then
    List<Task> tasksFound = taskServiceImpl.findAll("priority");

    assertNotNull(tasksFound);
    assertEquals(tasks.size(), tasksFound.size());
    assertEquals(Priority.HIGH.value, tasksFound.get(0).getPriority().value);
    verify(taskRepository, times(1)).findAll();
  }

  @Test
  void findAllByCreationTimeStamp() {
    Sort sort = Sort.by(Sort.Direction.ASC, "creationTimeStamp");

    Task task1 = Task.builder().description("Some task.").build();
    Task task2 = Task.builder().description("Some task.").build();
    Task task3 = Task.builder().description("Some task.").build();

    List<Task> tasks = List.of(task1, task2, task3);

    //when
    when(taskRepository.findAll(sort)).thenReturn(tasks);

    //then
    List<Task> tasksFound = taskServiceImpl.findAll("creationTimeStamp");

    assertNotNull(tasksFound);
    assertEquals(tasks.size(), tasksFound.size());
    verify(taskRepository, times(1)).findAll(sort);
  }

  @Test
  void findAllByInvalidField() {
    String invalidFieldName = "some_invalid_field";
    Sort sort = Sort.by(Sort.Direction.ASC, invalidFieldName);

    //when
    when(taskRepository.findAll(sort)).thenReturn(new ArrayList<>());

    //then
    assertThrows(
            IllegalArgumentException.class,
            () -> taskServiceImpl.findAll(invalidFieldName),
            "Task entities cannot be sorted by provided field: " + invalidFieldName);

    verify(taskRepository, times(0)).findAll(sort);
  }

  @Test
  void add() {
    Task task1 = Task.builder().build();

    //when
    when(taskRepository.save(any())).thenReturn(task1);

    //then
    Task returnedTask = taskServiceImpl.add(task1);

    assertNotNull(returnedTask);
    // check if TimeStamp was created upon adding new task
    assertNotNull(returnedTask.getCreationTimeStamp());
    verify(taskRepository, times(1)).save(any());
  }

  @Test
  void deleteById() {
    //when
    when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Task()));

    //then
    taskServiceImpl.deleteById(String.valueOf(UUID.randomUUID()));

    verify(taskRepository, times(1)).findById(any());
    verify(taskRepository, times(1)).delete(any());
  }

  @Test
  void deleteByIdNotFound() {
    String searchedUUID = String.valueOf(UUID.randomUUID());

    //when
    when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    //then
    assertThrows(
        TaskNotFoundException.class,
        () -> taskServiceImpl.deleteById(searchedUUID),
        "Task with id: \"" + searchedUUID +
            "\" couldn't be found.");

    verify(taskRepository, times(1)).findById(any());
    verify(taskRepository, times(0)).delete(any());
  }

  @Test
  void update() {
    //when
    when(taskRepository.findById(any())).thenReturn(Optional.of(new Task()));
    when(taskRepository.save(any())).thenReturn(new Task());

    //then
    Task updatedTask = taskServiceImpl.update(Task.builder().id(UUID.randomUUID()).build());

    assertNotNull(updatedTask);
    verify(taskRepository, times(1)).findById(any());
    verify(taskRepository, times(1)).save(any());
  }

  @Test
  void updateIdNotFound() {
    UUID searchedUUID = UUID.randomUUID();

    //when
    when(taskRepository.findById(any())).thenReturn(Optional.empty());

    //then
    assertThrows(
        TaskNotFoundException.class,
        () -> taskServiceImpl.update(Task.builder().id(searchedUUID).build()),
        "Task with id: \"" + searchedUUID +
            "\" couldn't be found.");

    verify(taskRepository, times(1)).findById(any());
    verify(taskRepository, times(0)).save(any());
  }
}
package tojoos.todolist.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import tojoos.todolist.pojo.Priority;
import tojoos.todolist.pojo.Task;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

  @Autowired
  TaskRepository taskRepository;

  @Test
  @DirtiesContext
  void testSave() {
    Task task1 = new Task();
    Task task2 = new Task();
    taskRepository.save(task1);
    taskRepository.save(task2);

    // check if id was generated automatically
    assertNotNull(task1.getId());
    assertEquals(2, taskRepository.findAll().size());
  }

  @Test
  @DirtiesContext
  void testFindById() {
    Task task1 = Task.builder().description("Some desc").priority(Priority.HIGH).build();
    taskRepository.save(task1);

    Task foundTask = taskRepository.findById(task1.getId()).orElse(null);

    assertNotNull(foundTask);
    assertEquals(task1.getDescription(), foundTask.getDescription());
  }

  @Test
  @DirtiesContext
  void testFindByIdNotFound() {
    Task task1 = Task.builder().description("Some desc").priority(Priority.HIGH).build();
    taskRepository.save(task1);

    Task foundTask = taskRepository.findById(UUID.randomUUID()).orElse(null);

    assertNotEquals(task1, foundTask);
  }

  @Test
  @DirtiesContext
  void testDeleteById() {
    Task task1 = Task.builder().build();
    Task task2 = Task.builder().build();
    taskRepository.save(task1);
    taskRepository.save(task2);

    taskRepository.deleteById(task1.getId());

    assertEquals(1, taskRepository.findAll().size());
    assertEquals(task2.getId(), taskRepository.findAll().get(0).getId());
  }

  @Test
  @DirtiesContext
  void testDelete() {
    Task task1 = Task.builder().build();
    Task task2 = Task.builder().build();
    taskRepository.save(task1);
    taskRepository.save(task2);

    taskRepository.delete(task1);

    assertEquals(1, taskRepository.findAll().size());
    assertEquals(task2.getId(), taskRepository.findAll().get(0).getId());
  }

  @Test
  @DirtiesContext
  void testUpdate() {
    Task task1 = Task.builder().description("Initial description.").build();
    taskRepository.save(task1);

    task1.setDescription("Changed description.");
    Task updatedTask = taskRepository.save(task1);

    assertEquals(1, taskRepository.findAll().size());
    assertEquals(task1.getId(), updatedTask.getId());
    assertEquals(task1.getDescription(), updatedTask.getDescription());
  }
}

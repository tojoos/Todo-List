package tojoos.todolist.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tojoos.todolist.exception.GlobalExceptionHandler;
import tojoos.todolist.exception.InvalidUUIDException;
import tojoos.todolist.exception.TaskNotFoundException;
import tojoos.todolist.pojo.Priority;
import tojoos.todolist.pojo.Task;
import tojoos.todolist.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
  @Mock
  TaskService taskService;

  @InjectMocks
  TaskController taskController;

  MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    taskController = new TaskController(taskService);
    mockMvc = MockMvcBuilders
            .standaloneSetup(taskController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void testTaskList() throws Exception {
    Task task1 = Task.builder().description("Task 1").build();
    Task task2 = Task.builder().description("Task 2").build();
    Task task3 = Task.builder().description("Task 3").build();

    //when
    when(taskService.findAll(any())).thenReturn(new ArrayList<>(List.of(task1, task2, task3)));

    //then
    MvcResult result = mockMvc.perform(get("/task/list"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    // Deserialize the JSON string into a List<Task>
    ObjectMapper objectMapper = new ObjectMapper();
    List<Task> taskList = objectMapper.readValue(contentAsString, new TypeReference<>() {});

    assertEquals(3, taskList.size());

    verify(taskService, times(1)).findAll(any());
  }

  @Test
  void testTaskListInvalidSortParam() throws Exception {
    String invalidSortBy = "invalidField";

    //when
    when(taskService.findAll(invalidSortBy)).thenThrow(
            new IllegalArgumentException("Task entities cannot be sorted by provided field: " + invalidSortBy));

    //then
    mockMvc.perform(get("/task/list?sortBy=" + invalidSortBy))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errorMessage",
                    equalTo("Task entities cannot be sorted by provided field: " + invalidSortBy)));

    verify(taskService, times(1)).findAll(any());
  }

  @Test
  void findById() throws Exception {
    UUID uuid = UUID.randomUUID();
    Task task = Task.builder().id(uuid).description("Some desc.").priority(Priority.HIGH).build();

    //when
    when(taskService.findById(any()))
            .thenReturn(task);

    //then
    mockMvc.perform(get("/task/" + uuid))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.description",
                    equalTo(task.getDescription())));

    verify(taskService, times(1)).findById(String.valueOf(uuid));
  }

  @Test
  void findByIdNotFound() throws Exception {
    String invalidUUid = "11111111-2222-3333-4444-555555555555";

    //when
    when(taskService.findById(any()))
            .thenThrow(new TaskNotFoundException("Task with id: \"" + invalidUUid +
                    "\" couldn't be found."));

    //then
    mockMvc.perform(get("/task/" + invalidUUid))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errorMessage",
                    equalTo("Task with id: \"" + invalidUUid
                            + "\" couldn't be found.")));

    verify(taskService, times(1)).findById(invalidUUid);
  }

  @Test
  void add() throws Exception {
    UUID uuid = UUID.randomUUID();
    Task task = Task.builder().id(uuid).description("Some desc.").build();

    //when
    when(taskService.add(any(Task.class))).thenReturn(task);

    //then
    mockMvc.perform(post("/task/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(task)))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.priority",
                    equalTo(null)))
            .andExpect(jsonPath("$.id",
                    equalTo(String.valueOf(uuid))));

    verify(taskService, times(1)).add(any(Task.class));
  }

  @Test
  void addMissingRequestBody() throws Exception {
    //then
    mockMvc.perform(post("/task/"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp",
                    Matchers.notNullValue()))
            .andExpect(jsonPath("$.errorMessage",
                    Matchers.containsString("Required request body is missing")));

    verify(taskService, times(0)).add(any(Task.class));
  }

  @Test
  void update() throws Exception {
    UUID uuid = UUID.randomUUID();
    Task task = Task.builder().id(uuid).description("Updated desc.").build();

    //when
    when(taskService.update(any(Task.class))).thenReturn(task);

    //then
    mockMvc.perform(put("/task/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(task)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.priority",
                    equalTo(null)))
            .andExpect(jsonPath("$.id",
                    equalTo(String.valueOf(uuid))));

    verify(taskService, times(1)).update(any(Task.class));
  }

  @Test
  void updateInvalidRequestBody() throws Exception {
    Task task = Task.builder().description("Invalid task to update without id field").build();
    InvalidUUIDException exception = new InvalidUUIDException("Provided value of id = \"null\" is invalid.");

    //when
    when(taskService.update(any(Task.class))).thenThrow(exception);

    //then
    mockMvc.perform(put("/task/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(task)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage",
                    equalTo(exception.getErrorMessage())));

    verify(taskService, times(1)).update(any(Task.class));
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
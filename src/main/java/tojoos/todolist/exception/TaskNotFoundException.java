package tojoos.todolist.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {
  private final String errorMessage;

  public TaskNotFoundException(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}

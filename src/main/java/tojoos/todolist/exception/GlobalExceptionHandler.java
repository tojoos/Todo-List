package tojoos.todolist.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TaskNotFoundException.class)
  public final ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex, HttpServletRequest request) {
    ApiError apiError = new ApiError(
        LocalDateTime.now(),
        request.getRequestURI(),
        HttpStatus.NOT_FOUND,
        ex.getErrorMessage()
    );
    log.error(ex.getErrorMessage());
    return new ResponseEntity<>(apiError, apiError.status());
  }

  @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
  public final ResponseEntity<Object> handleInvalidIllegalArgumentAndHttpMessageNotReadableException(RuntimeException ex, HttpServletRequest request) {
    ApiError apiError = new ApiError(
        LocalDateTime.now(),
        request.getRequestURI(),
        HttpStatus.BAD_REQUEST,
        ex.getMessage()
    );
    log.error(ex.getMessage());
    return new ResponseEntity<>(apiError, apiError.status());
  }

  @ExceptionHandler({InvalidUUIDException.class})
  public final ResponseEntity<Object> handleInvalidUUIDException(InvalidUUIDException ex, HttpServletRequest request) {
    ApiError apiError = new ApiError(
            LocalDateTime.now(),
            request.getRequestURI(),
            HttpStatus.BAD_REQUEST,
            ex.getErrorMessage()
    );
    log.error(ex.getErrorMessage());
    return new ResponseEntity<>(apiError, apiError.status());
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public final ResponseEntity<Object> handleDefaultException(Exception ex, HttpServletRequest request) {
    ApiError apiError = new ApiError(
        LocalDateTime.now(),
        request.getRequestURI(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage()
    );
    log.error(ex.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

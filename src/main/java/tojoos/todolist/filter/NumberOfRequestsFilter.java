package tojoos.todolist.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tojoos.todolist.service.RequestCountService;

import java.io.IOException;

@Slf4j
@Component
public class NumberOfRequestsFilter extends OncePerRequestFilter {

  private final RequestCountService requestCountService;

  public NumberOfRequestsFilter(RequestCountService requestCountService) {
    this.requestCountService = requestCountService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // count all request sent to the dedicated '/task/' rest api
    if (request.getRequestURI().contains("/task/")) {
      log.info("Incoming request, [HTTP {}], URI: {}", request.getMethod(), request.getRequestURI());
      requestCountService.incrementRequestCount();
    }
    filterChain.doFilter(request, response);
  }
}

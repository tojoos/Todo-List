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

@Component
public class NumberOfRequestsFilter extends OncePerRequestFilter {

  private final RequestCountService requestCountService;

  public NumberOfRequestsFilter(RequestCountService requestCountService) {
    this.requestCountService = requestCountService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    requestCountService.incrementRequestCount();
    filterChain.doFilter(request, response);
  }
}

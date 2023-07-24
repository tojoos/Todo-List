package tojoos.todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tojoos.todolist.service.RequestCountService;

@RestController
@RequestMapping("/requestCount")
public class RequestCountController {

    private final RequestCountService requestCountService;

    public RequestCountController(RequestCountService requestCountService) {
        this.requestCountService = requestCountService;
    }

    @GetMapping("")
    public ResponseEntity<Long> getRequestCount() {
        Long requestCount = requestCountService.getRequestCount();
        return new ResponseEntity<>(requestCount, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Long> incrementRequestCount() {
        Long requestCount = requestCountService.incrementRequestCount();
        return new ResponseEntity<>(requestCount, HttpStatus.OK);
    }
}

package tojoos.todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tojoos.todolist.service.RequestCountService;

@RestController
@RequestMapping("/requestCount")
@CrossOrigin("*")
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
}

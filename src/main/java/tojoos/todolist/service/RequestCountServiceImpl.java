package tojoos.todolist.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tojoos.todolist.pojo.RequestCount;
import tojoos.todolist.repository.RequestCountRepository;


@Slf4j
@Service
public class RequestCountServiceImpl implements RequestCountService {

    private final RequestCountRepository requestCountRepository;

    private final Long requestCountId = 1L;

    public RequestCountServiceImpl(RequestCountRepository requestCountRepository) {
        this.requestCountRepository = requestCountRepository;
    }

    @Override
    public Long incrementRequestCount() {
        // ensure requestCount entity exists
        requestCountExists();
        Long requestCount = requestCountRepository.save(new RequestCount(requestCountId, getRequestCount() + 1)).getValue();

        log.info("Incrementing request count, current value = {}", requestCount);
        return requestCount;
    }

    @Override
    public Long getRequestCount() {
        if (requestCountExists()) {
            RequestCount requestCount = requestCountRepository.findById(requestCountId)
                    .orElseThrow(() -> new EntityNotFoundException("Request entity not found."));
            log.info("Getting current request count = {}", requestCount.getValue());

            return requestCount.getValue();
        } else {
            return 0L;
        }
    }

    private boolean requestCountExists() {
        RequestCount requestCount = requestCountRepository.findById(requestCountId).orElse(null);
        if (requestCount == null) {
            // create new entity if it doesn't exist
            log.info("Initializing request count entity.");
            requestCountRepository.save(new RequestCount(requestCountId, 0L));
        }
        return requestCount != null;
    }
}

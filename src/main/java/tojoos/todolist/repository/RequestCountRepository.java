package tojoos.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tojoos.todolist.pojo.RequestCount;

@Repository
public interface RequestCountRepository extends JpaRepository<RequestCount, Long> {
}

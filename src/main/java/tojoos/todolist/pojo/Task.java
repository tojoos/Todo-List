package tojoos.todolist.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true)
  @JdbcTypeCode(java.sql.Types.VARCHAR)
  private UUID id;

  private String title;

  @Lob
  @Column(columnDefinition="TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  private Priority priority;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
  private LocalDateTime creationTimeStamp;
}

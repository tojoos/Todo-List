package tojoos.todolist.pojo;

public enum Priority {
  LOW(1), MEDIUM(5),  HIGH(10);

  public final Integer value;
  Priority(Integer value) {
    this.value = value;
  }
}

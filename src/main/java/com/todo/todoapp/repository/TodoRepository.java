package com.todo.todoapp.repository;

import com.todo.todoapp.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByDueDate(LocalDate dueDate);
    List<Todo> findByCompleted(boolean completed);
    List<Todo> findByCompletedFalseAndDueDateLessThanEqual(LocalDate date);
} 
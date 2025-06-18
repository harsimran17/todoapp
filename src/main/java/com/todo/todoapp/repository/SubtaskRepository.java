package com.todo.todoapp.repository;

import com.todo.todoapp.model.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTodoId(Long todoId);
} 
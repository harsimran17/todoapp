package com.todo.todoapp.service;

import com.todo.todoapp.model.Todo;
import com.todo.todoapp.model.Subtask;
import com.todo.todoapp.repository.TodoRepository;
import com.todo.todoapp.repository.SubtaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private SubtaskRepository subtaskRepository;
    
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }
    
    public List<Todo> getTodosByDate(LocalDate date) {
        return todoRepository.findByDueDate(date);
    }
    
    public List<Todo> getBacklogTodos() {
        return todoRepository.findByCompletedFalseAndDueDateLessThanEqualAndWontDoFalseOrderByDueDateAsc(LocalDate.now());
    }
    
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }
    
    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }
    
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
    
    public Todo updateTodo(Long id, Todo todoDetails) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setDueDate(todoDetails.getDueDate());
        todo.setCompleted(todoDetails.isCompleted());
        
        return todoRepository.save(todo);
    }
    
    // Subtask methods
    public Subtask addSubtask(Long todoId, Subtask subtask) {
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new RuntimeException("Todo not found"));
        subtask.setTodo(todo);
        return subtaskRepository.save(subtask);
    }
    
    public Subtask updateSubtask(Long subtaskId, Subtask subtaskDetails) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
            .orElseThrow(() -> new RuntimeException("Subtask not found"));
        
        if (subtaskDetails.getTitle() != null) {
            subtask.setTitle(subtaskDetails.getTitle());
        }
        if (subtaskDetails.getDescription() != null) {
            subtask.setDescription(subtaskDetails.getDescription());
        }
        // Use Boolean wrapper to detect intent when coming from partial update
        try {
            // If caller set explicit completed state, honor it
            boolean completed = subtaskDetails.isCompleted();
            // We cannot differentiate default false vs not provided with primitive boolean,
            // so keep as-is unless title/description only. In practice our controller sets it when present.
            subtask.setCompleted(completed);
        } catch (Exception ignored) { }
        
        return subtaskRepository.save(subtask);
    }
    
    public void deleteSubtask(Long subtaskId) {
        subtaskRepository.deleteById(subtaskId);
    }
    
    public Subtask toggleSubtaskStatus(Long subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
            .orElseThrow(() -> new RuntimeException("Subtask not found"));
        subtask.setCompleted(!subtask.isCompleted());
        Subtask savedSubtask = subtaskRepository.save(subtask);
        
        // Check and update parent todo status
        updateParentTodoStatus(subtask.getTodo().getId());
        
        return savedSubtask;
    }
    
    private void updateParentTodoStatus(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new RuntimeException("Todo not found"));
            
        List<Subtask> subtasks = subtaskRepository.findByTodoId(todoId);
        
        if (!subtasks.isEmpty()) {
            boolean allSubtasksCompleted = subtasks.stream()
                .allMatch(Subtask::isCompleted);
            
            if (allSubtasksCompleted != todo.isCompleted()) {
                todo.setCompleted(allSubtasksCompleted);
                todoRepository.save(todo);
            }
        }
    }
    
    public List<Subtask> getSubtasksByTodoId(Long todoId) {
        return subtaskRepository.findByTodoId(todoId);
    }
    
    public Todo toggleWontDoStatus(Long id) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found"));
        todo.setWontDo(!todo.isWontDo());
        return todoRepository.save(todo);
    }
} 
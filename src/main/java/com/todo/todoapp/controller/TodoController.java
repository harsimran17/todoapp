package com.todo.todoapp.controller;

import com.todo.todoapp.model.Todo;
import com.todo.todoapp.model.Subtask;
import com.todo.todoapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String listTodos(Model model) {
        model.addAttribute("todos", todoService.getAllTodos());
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("newSubtask", new Subtask());
        return "todos";
    }

    @GetMapping("/date/{date}")
    public String listTodosByDate(@PathVariable String date, Model model) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            model.addAttribute("todos", todoService.getTodosByDate(localDate));
            model.addAttribute("newTodo", new Todo());
            model.addAttribute("newSubtask", new Subtask());
            model.addAttribute("selectedDate", date);
            return "todos";
        } catch (Exception e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/backlog")
    public String listBacklogTodos(Model model) {
        model.addAttribute("todos", todoService.getBacklogTodos());
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("newSubtask", new Subtask());
        model.addAttribute("isBacklog", true);
        return "todos";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo created = todoService.saveTodo(todo);
        return ResponseEntity.created(URI.create("/todos/" + created.getId())).body(created);
    }

    @GetMapping("/edit/{id}")
    public String editTodoForm(@PathVariable Long id, Model model) {
        model.addAttribute("todo", todoService.getTodoById(id).orElseThrow());
        model.addAttribute("newSubtask", new Subtask());
        return "edit-todo";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo updated = todoService.updateTodo(id, todo);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Todo> patchTodo(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Todo todo = todoService.getTodoById(id).orElseThrow();

        if (updates.containsKey("title")) {
            todo.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            todo.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("dueDate")) {
            // Expecting ISO date string
            todo.setDueDate(LocalDate.parse((String) updates.get("dueDate")));
        }
        if (updates.containsKey("completed")) {
            todo.setCompleted(Boolean.TRUE.equals(updates.get("completed")));
        }
        if (updates.containsKey("wontDo")) {
            todo.setWontDo(Boolean.TRUE.equals(updates.get("wontDo")));
        }

        Todo saved = todoService.saveTodo(todo);
        return ResponseEntity.ok(saved);
    }
    
    // Subtask endpoints
    @PostMapping("/{todoId}/subtasks")
    @ResponseBody
    public ResponseEntity<Subtask> addSubtask(@PathVariable Long todoId, @RequestBody Subtask subtask) {
        Subtask created = todoService.addSubtask(todoId, subtask);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/subtasks/" + created.getId()))
                .body(created);
    }
    
    @PatchMapping("/subtasks/{subtaskId}")
    @ResponseBody
    public ResponseEntity<Subtask> updateSubtask(@PathVariable Long subtaskId, @RequestBody Map<String, Object> updates) {
        // Allow partial updates: title, description, completed
        Subtask details = new Subtask();
        if (updates.containsKey("title")) {
            details.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            details.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("completed")) {
            details.setCompleted(Boolean.TRUE.equals(updates.get("completed")));
        }
        Subtask updated = todoService.updateSubtask(subtaskId, details);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/subtasks/{subtaskId}")
    @ResponseBody
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long subtaskId) {
        todoService.deleteSubtask(subtaskId);
        return ResponseEntity.noContent().build();
    }
    
    // (Removed duplicate PATCH mapping for subtasks)

    @PatchMapping("/{id}/wontdo")
    @ResponseBody
    public ResponseEntity<Todo> toggleWontDoStatus(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> body) {
        if (body != null && body.containsKey("wontDo")) {
            Todo todo = todoService.getTodoById(id).orElseThrow();
            todo.setWontDo(Boolean.TRUE.equals(body.get("wontDo")));
            return ResponseEntity.ok(todoService.saveTodo(todo));
        }
        return ResponseEntity.ok(todoService.toggleWontDoStatus(id));
    }
  
    @GetMapping("/{todoId}/subtasks")
    @ResponseBody
    public List<Subtask> getSubtasksByTodoId(@PathVariable Long todoId) {
        return todoService.getSubtasksByTodoId(todoId);
    }
} 
package com.todo.todoapp.controller;

import com.todo.todoapp.model.Todo;
import com.todo.todoapp.model.Subtask;
import com.todo.todoapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
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

    @PostMapping
    @ResponseBody
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @GetMapping("/edit/{id}")
    public String editTodoForm(@PathVariable Long id, Model model) {
        model.addAttribute("todo", todoService.getTodoById(id).orElseThrow());
        model.addAttribute("newSubtask", new Subtask());
        return "edit-todo";
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Boolean> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return Map.of("success", true);
    }

    @GetMapping("/toggle/{id}")
    @ResponseBody
    public Todo toggleTodoStatus(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id).orElseThrow();
        todo.setCompleted(!todo.isCompleted());
        return todoService.saveTodo(todo);
    }
    
    // Subtask endpoints
    @PostMapping("/{todoId}/subtasks")
    @ResponseBody
    public Subtask addSubtask(@PathVariable Long todoId, @RequestBody Subtask subtask) {
        return todoService.addSubtask(todoId, subtask);
    }
    
    @PostMapping("/subtasks/{subtaskId}/update")
    @ResponseBody
    public Subtask updateSubtask(@PathVariable Long subtaskId, @RequestBody Subtask subtask) {
        return todoService.updateSubtask(subtaskId, subtask);
    }
    
    @GetMapping("/subtasks/{subtaskId}/delete")
    @ResponseBody
    public Map<String, Boolean> deleteSubtask(@PathVariable Long subtaskId) {
        todoService.deleteSubtask(subtaskId);
        return Map.of("success", true);
    }
    
    @GetMapping("/subtasks/{subtaskId}/toggle")
    @ResponseBody
    public Subtask toggleSubtaskStatus(@PathVariable Long subtaskId) {
        return todoService.toggleSubtaskStatus(subtaskId);
    }
} 
package com.todo.todoapp.controller;

import com.todo.todoapp.model.Todo;
import com.todo.todoapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String listTodos(Model model) {
        model.addAttribute("todos", todoService.getAllTodos());
        model.addAttribute("newTodo", new Todo());
        return "todos";
    }

    @GetMapping("/date/{date}")
    public String listTodosByDate(@PathVariable String date, Model model) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            model.addAttribute("todos", todoService.getTodosByDate(localDate));
            model.addAttribute("newTodo", new Todo());
            model.addAttribute("selectedDate", date);
            return "todos";
        } catch (Exception e) {
            return "redirect:/todos";
        }
    }

    @PostMapping
    public String createTodo(@ModelAttribute Todo todo) {
        todoService.saveTodo(todo);
        return "redirect:/todos";
    }

    @GetMapping("/edit/{id}")
    public String editTodoForm(@PathVariable Long id, Model model) {
        model.addAttribute("todo", todoService.getTodoById(id).orElseThrow());
        return "edit-todo";
    }

    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable Long id, @ModelAttribute Todo todo) {
        todoService.updateTodo(id, todo);
        return "redirect:/todos";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return "redirect:/todos";
    }

    @GetMapping("/toggle/{id}")
    public String toggleTodoStatus(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id).orElseThrow();
        todo.setCompleted(!todo.isCompleted());
        todoService.saveTodo(todo);
        return "redirect:/todos";
    }
} 
package com.todo.todoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootRedirectController {
    @GetMapping("/")
    public RedirectView redirectToTodos() {
        return new RedirectView("/todos");
    }
} 

package com.taskflow.web.controller;

import com.taskflow.core.model.Task;
import com.taskflow.core.model.TaskStatus;
import com.taskflow.core.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String dashboard(
            @RequestParam(name = "status", required = false) String status,
            Model model) {

        if ("PENDING".equalsIgnoreCase(status)) {
            model.addAttribute("tasks", taskService.findByStatus(TaskStatus.PENDING));
        } else if ("COMPLETED".equalsIgnoreCase(status)) {
            model.addAttribute("tasks", taskService.findByStatus(TaskStatus.COMPLETED));
        } else {
            model.addAttribute("tasks", taskService.findAll());
        }

        model.addAttribute("task", new Task());
        model.addAttribute("totalCount", taskService.countAll());
        model.addAttribute("pendingCount", taskService.countByStatus(TaskStatus.PENDING));
        model.addAttribute("completedCount", taskService.countByStatus(TaskStatus.COMPLETED));
        model.addAttribute("activeFilter", status == null ? "ALL" : status.toUpperCase());

        return "index";
    }

    @PostMapping("/tasks")
    public String create(
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", result.getFieldError().getDefaultMessage());
            return "redirect:/";
        }

        taskService.create(task);
        redirect.addFlashAttribute("message", "Task created successfully.");

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggle(
            @PathVariable("id") Long id,
            RedirectAttributes redirect) {

        taskService.toggleStatus(id);
        redirect.addFlashAttribute("message", "Task status updated.");

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes redirect) {

        taskService.delete(id);
        redirect.addFlashAttribute("message", "Task deleted.");

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/edit")
    public String edit(
            @PathVariable("id") Long id,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description", required = false) String description,
            RedirectAttributes redirect) {

        if (title == null || title.isBlank() || title.length() > 100) {
            redirect.addFlashAttribute("error",
                    "Title is required and must be at most 100 characters.");
            return "redirect:/";
        }

        if (description != null && description.length() > 500) {
            redirect.addFlashAttribute("error",
                    "Description must be at most 500 characters.");
            return "redirect:/";
        }

        Task updated = new Task(title.trim(), description);
        taskService.update(id, updated);
        redirect.addFlashAttribute("message", "Task updated.");

        return "redirect:/";
    }
}


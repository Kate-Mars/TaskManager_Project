package com.example.taskmanagerfx.service;

import com.example.taskmanagerfx.model.Task;
import com.example.taskmanagerfx.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Task Management Service
 * Handles task-related operations for the current user:
 * - CRUD operations for tasks
 * - Task search and filtering
 * - Task sorting algorithms
 * - Date validation and formatting
 *
 * Works in conjunction with UserManager to ensure
 * task data is properly scoped to each user.
 */
public class TaskManager {
    private final User currentUser;
    private final UserManager userManager;

    public TaskManager(User currentUser, UserManager userManager) {
        this.currentUser = currentUser;
        this.userManager = userManager;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<Task> getTasks() {
        return currentUser.getTasks();
    }

    public void addTask(String title, String description, String date, int priority) {
        Task task = new Task(title, description, date, priority);
        currentUser.addTask(task);
        userManager.saveToFile();
    }

    public void deleteTask(Task task) {
        currentUser.removeTask(task);
        userManager.saveToFile();
    }

    public void updateTask(Task task, String title, String description, String date, int priority) {
        task.setTitle(title);
        task.setDescription(description);
        task.setDate(date);
        task.setPriority(priority);
        userManager.saveToFile();
    }

    public List<Task> sortTasksByDeadlineAsc() {
        List<Task> sorted = new ArrayList<>(getTasks());
        sorted.sort(Comparator.comparing(task -> parseDate(task.getDate())));
        return sorted;
    }

    public List<Task> sortTasksByDeadlineDesc() {
        List<Task> sorted = new ArrayList<>(getTasks());
        sorted.sort((task1, task2) -> {
            LocalDateTime date1 = parseDate(task1.getDate());
            LocalDateTime date2 = parseDate(task2.getDate());
            return date2.compareTo(date1);
        });
        return sorted;
    }

    public List<Task> sortTasksByPriorityAsc() {
        List<Task> sorted = new ArrayList<>(getTasks());
        sorted.sort(Comparator.comparingInt(Task::getPriority));
        return sorted;
    }

    public List<Task> sortTasksByPriorityDesc() {
        List<Task> sorted = new ArrayList<>(getTasks());
        sorted.sort((task1, task2) -> Integer.compare(task2.getPriority(), task1.getPriority()));
        return sorted;
    }

    public List<Task> searchTasksByTitle(String query) {
        return getTasks().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Task> searchTasksByDescription(String query) {
        return getTasks().stream()
                .filter(task -> task.getDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Task> searchTasksByPriority(int priority) {
        return getTasks().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public boolean isValidDate(String date) {
        return parseDate(date) != null;
    }

    public LocalDateTime parseDate(String date) {
        String[] patterns = {
                "dd.MM.yyyy H:mm", "dd.MM.yyyy HH:mm",
                "d.M.yyyy H:mm", "d.M.yyyy HH:mm"
        };

        for (String p : patterns) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
                return LocalDateTime.parse(date, fmt);
            } catch (DateTimeParseException ignored) { }
        }
        return null;
    }

    public boolean isPastDate(String date) {
        LocalDateTime dateTime = parseDate(date);
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }
}
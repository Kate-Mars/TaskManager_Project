package com.example.taskmanagerfx.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system.
 * Stores user credentials and maintains a personal task list.
 *
 * Class fields:
 * - username: unique user identifier
 * - password: authentication password
 * - tasks: collection of user's tasks
 *
 * Provides task management methods:
 * - Add new tasks to personal collection
 * - Remove tasks by index
 * - Retrieve tasks by index
 *
 * @version 2.0
 */
public class User {
    private String username;
    private String password;
    private List<Task> tasks;

    public User() {
        this.tasks = new ArrayList<>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks = new ArrayList<>();
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    // Task management methods
    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}
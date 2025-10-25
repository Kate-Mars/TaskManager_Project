package com.example.taskmanagerfx.service;

import com.example.taskmanagerfx.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * User Management Service
 * Handles user-related operations:
 * - User registration and authentication
 * - Current user session management
 * - User data persistence
 * This service maintains the application's user state
 * and provides access control for task management.
 */
public class UserManager {
    private List<User> users;
    private final String FILE_NAME = "users.json";
    private final Gson gson;
    private User currentUser;

    public UserManager() {
        this.users = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadFromFile();
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.currentUser = user;
                return user;
            }
        }
        return null;
    }

    public User register(String username, String password) {
        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return null;
            }
        }

        User newUser = new User(username, password);
        users.add(newUser);
        this.currentUser = newUser;
        saveToFile();
        return newUser;
    }

    public void logout() {
        saveToFile();
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Файл не найден. Будет создан новый при сохранении.");
            return;
        }

        try (Reader reader = new FileReader(FILE_NAME)) {
            // Проверяем, не пустой ли файл
            if (file.length() == 0) {
                System.out.println("Файл пустой. Будет создан новый при сохранении.");
                return;
            }

            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> loadedUsers = gson.fromJson(reader, userListType);
            if (loadedUsers != null) {
                this.users = loadedUsers;
                System.out.println("Данные успешно загружены из " + FILE_NAME);
            } else {
                this.users = new ArrayList<>();
                System.out.println("Файл пустой или содержит null. Создан новый список пользователей.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            this.users = new ArrayList<>();
        } catch (com.google.gson.JsonSyntaxException e) {
            System.out.println("Ошибка парсинга JSON: " + e.getMessage() + ". Создан новый список пользователей.");
            this.users = new ArrayList<>();
        }
    }

    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(users, writer);
            System.out.println("Данные успешно сохранены в " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    public List<User> getUsers() {
        return users;
    }
}
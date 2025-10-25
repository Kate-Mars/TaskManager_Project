package com.example.taskmanagerfx.controllers;

import com.example.taskmanagerfx.service.UserManager;
import com.example.taskmanagerfx.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Login Controller for user authentication and registration.
 * Manages the login screen functionality including:
 * - User login with credentials validation
 * - New user registration
 * - Test/demo account access
 * - Navigation to main application screen
 *
 * Integrated with UserManager for user authentication
 * and data persistence.
 *
 * Security Note: In a production environment, consider
 * implementing password encryption and more robust
 * authentication mechanisms.
 */
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private UserManager userManager;
    private Stage stage;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Заполните все поля");
            return;
        }

        User user = userManager.login(username, password);
        if (user != null) {
            showMainApplication(user);
        } else {
            showAlert("Ошибка", "Неверное имя пользователя или пароль");
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Заполните все поля");
            return;
        }

        User user = userManager.register(username, password);
        if (user != null) {
            showAlert("Успех", "Пользователь " + username + " успешно зарегистрирован!");
            showMainApplication(user);
        } else {
            showAlert("Ошибка", "Пользователь с таким именем уже существует");
        }
    }

    @FXML
    private void handleTestLogin() {
        // Автоматический вход с тестовыми данными
        User user = userManager.login("test", "test");
        if (user == null) {
            // Создаем тестового пользователя если его нет
            user = userManager.register("test", "test");
        }
        if (user != null) {
            showMainApplication(user);
        }
    }

    @FXML
    private void handleExit() {
        userManager.saveToFile();
        stage.close();
    }

    private void showMainApplication(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/taskmanagerfx/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setUserManager(userManager);
            mainController.setStage(stage);
            mainController.initializeData();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("To-Do List - " + user.getUsername());

            // Растягиваем главное окно на весь экран
            stage.setWidth(1920);
            stage.setHeight(1080);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить главное окно: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
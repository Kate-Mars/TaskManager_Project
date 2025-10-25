package app.controller;

import app.model.User;
import app.model.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private final UserManager userManager = new UserManager();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userManager.login(username, password);
        System.out.println("Logged in: " + user.getUsername());
    }
}


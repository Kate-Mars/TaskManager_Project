package com.example.taskmanagerfx;

import com.example.taskmanagerfx.controllers.LoginController;
import com.example.taskmanagerfx.service.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private UserManager userManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        userManager = new UserManager();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/taskmanagerfx/login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setUserManager(userManager);
        controller.setStage(primaryStage);

        Scene scene = new Scene(root);

        primaryStage.setTitle("To-Do List System");
        primaryStage.setScene(scene);

        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (userManager != null) {
            userManager.saveToFile();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
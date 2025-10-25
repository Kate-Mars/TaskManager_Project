package app;

import app.controller.LoginController;
import app.util.FileStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FileStorage.initialize();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("app\\view\\login.fxml"));
        System.out.println(getClass().getResource("app\\view\\login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Task Manager");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FileStorage.saveAll();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package app.controller;

import app.model.Task;
import app.model.TaskManager;
import app.model.User;
import app.model.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class TaskController {

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, String> titleCol;

    @FXML
    private TableColumn<Task, String> descCol;

    @FXML
    private TableColumn<Task, String> dateCol;

    @FXML
    private TableColumn<Task, Integer> priorityCol;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button deleteTaskButton;

    private User currentUser;
    private TaskManager taskManager;
    private UserManager userManager;
    private ObservableList<Task> tasks;

    public void setUserManager(UserManager um) { this.userManager = um; }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.taskManager = new TaskManager(user);
        List<Task> list = taskManager.getTasks();
        tasks = FXCollections.observableArrayList(list);
        taskTable.setItems(tasks);
    }

    @FXML
    private void handleAddTask() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Task");
        dialog.setHeaderText("Enter task title:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(title -> {
            Task t = new Task(title);
            taskManager.addTask(t);
            tasks.add(t);
        });
    }

    @FXML
    private void handleDeleteTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tasks.remove(selected);
        }
    }
}

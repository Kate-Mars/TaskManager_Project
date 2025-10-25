package com.example.taskmanagerfx.controllers;

import com.example.taskmanagerfx.service.UserManager;
import com.example.taskmanagerfx.service.TaskManager;
import com.example.taskmanagerfx.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 * Main Controller for the Task Management Application.
 * Handles the core business logic for task operations:
 * - Adding, editing, and deleting tasks
 * - Searching and sorting tasks
 * - Displaying tasks in a table view
 * - User interaction through JavaFX interface
 *
 * The controller is integrated with UserManager for user management
 * and TaskManager for handling tasks of the current user.
 *
 * Key Features:
 * - Real-time task count updates
 * - Date validation with past-date confirmation
 * - Multiple search types (by title, description, priority)
 * - Multiple sorting options (by deadline, priority)
 * - Seamless navigation between login and main screens
 */
public class MainController {
    @FXML private Label welcomeLabel;
    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descriptionColumn;
    @FXML private TableColumn<Task, String> dateColumn;
    @FXML private TableColumn<Task, String> priorityColumn;
    @FXML private TableColumn<Task, String> deadlineColumn;
    @FXML private Label taskCountLabel;

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Integer> hourComboBox;
    @FXML private ComboBox<Integer> minuteComboBox;
    @FXML private ComboBox<Integer> priorityComboBox;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;
    @FXML private ComboBox<String> sortComboBox;

    private UserManager userManager;
    private TaskManager taskManager;
    private Stage stage;
    private ObservableList<Task> tasks;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priorityString"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDeadline"));

        tasksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Initialize combo boxes
        priorityComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        // Initialize time pickers
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        hourComboBox.setItems(hours);

        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            minutes.add(i);
        }
        minuteComboBox.setItems(minutes);

        searchTypeComboBox.setItems(FXCollections.observableArrayList(
                "По названию", "По описанию", "По приоритету"
        ));
        sortComboBox.setItems(FXCollections.observableArrayList(
                "Ближайшие к дедлайну", "Дальние к дедлайну",
                "По возрастанию приоритета", "По убыванию приоритета"
        ));

        // Set default values
        searchTypeComboBox.setValue("По названию");
        sortComboBox.setValue("Ближайшие к дедлайну");

        // Set current date as default
        datePicker.setValue(LocalDate.now());

        // Set current time as default
        hourComboBox.setValue(LocalTime.now().getHour());
        minuteComboBox.setValue(LocalTime.now().getMinute());

        // Добавьте слушатель для таблицы задач
        tasksTable.itemsProperty().addListener((obs, oldVal, newVal) -> {
            updateTaskCount();
        });
    }

    private void updateTaskCount() {
        int count = tasksTable.getItems().size();
        taskCountLabel.setText(count + " " + getTaskWord(count));
    }

    private String getTaskWord(int count) {
        if (count % 10 == 1 && count % 100 != 11) return "задача";
        if (count % 10 >= 2 && count % 10 <= 4 && (count % 100 < 10 || count % 100 >= 20)) return "задачи";
        return "задач";
    }

    public void initializeData() {
        if (userManager != null && userManager.getCurrentUser() != null) {
            taskManager = new TaskManager(userManager.getCurrentUser(), userManager);
            welcomeLabel.setText("Добро пожаловать, " + userManager.getCurrentUser().getUsername() + "!");
            refreshTasks();
        }
    }

    @FXML
    private void handleAddTask() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        Integer priority = priorityComboBox.getValue();
        LocalDate date = datePicker.getValue();
        Integer hour = hourComboBox.getValue();
        Integer minute = minuteComboBox.getValue();

        if (title.isEmpty() || date == null || hour == null || minute == null || priority == null) {
            showAlert("Ошибка", "Заполните все обязательные поля");
            return;
        }

        // Форматируем дату и время
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedDate = dateTime.format(formatter);

        if (!taskManager.isValidDate(formattedDate)) {
            showAlert("Ошибка", "Неверный формат даты");
            return;
        }

        if (taskManager.isPastDate(formattedDate)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText("Внимание");
            alert.setContentText("Вы устанавливаете дедлайн в прошлом. Продолжить?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                taskManager.addTask(title, description, formattedDate, priority);
                clearForm();
                refreshTasks();
            }
        } else {
            taskManager.addTask(title, description, formattedDate, priority);
            clearForm();
            refreshTasks();
        }
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Удалить задачу?");
            alert.setContentText("Вы уверены, что хотите удалить задачу: " + selectedTask.getTitle());

            if (alert.showAndWait().get() == ButtonType.OK) {
                taskManager.deleteTask(selectedTask);
                refreshTasks();
            }
        } else {
            showAlert("Ошибка", "Выберите задачу для удаления");
        }
    }

    @FXML
    private void handleEditTask() {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            // Заполняем форму выбранной задачей
            titleField.setText(selectedTask.getTitle());
            descriptionField.setText(selectedTask.getDescription());

            // Парсим дату из задачи
            String taskDate = selectedTask.getDate();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(taskDate, formatter);
                datePicker.setValue(dateTime.toLocalDate());
                hourComboBox.setValue(dateTime.getHour());
                minuteComboBox.setValue(dateTime.getMinute());
            } catch (Exception e) {
                // Если не удалось распарсить, оставляем текущие значения
                System.out.println("Ошибка парсинга даты: " + e.getMessage());
            }

            priorityComboBox.setValue(selectedTask.getPriority());

            // Удаляем старую задачу при сохранении изменений
            taskManager.deleteTask(selectedTask);
        } else {
            showAlert("Ошибка", "Выберите задачу для редактирования");
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        String searchType = searchTypeComboBox.getValue();

        if (query.isEmpty()) {
            refreshTasks();
            return;
        }

        ObservableList<Task> searchResults = FXCollections.observableArrayList();

        switch (searchType) {
            case "По названию":
                searchResults.addAll(taskManager.searchTasksByTitle(query));
                break;
            case "По описанию":
                searchResults.addAll(taskManager.searchTasksByDescription(query));
                break;
            case "По приоритету":
                try {
                    int priority = Integer.parseInt(query);
                    searchResults.addAll(taskManager.searchTasksByPriority(priority));
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Введите число от 1 до 5 для поиска по приоритету");
                    return;
                }
                break;
        }

        tasksTable.setItems(searchResults);
    }

    @FXML
    private void handleSort() {
        String sortType = sortComboBox.getValue();
        ObservableList<Task> sortedTasks = FXCollections.observableArrayList();

        switch (sortType) {
            case "Ближайшие к дедлайну":
                sortedTasks.addAll(taskManager.sortTasksByDeadlineAsc());
                break;
            case "Дальние к дедлайну":
                sortedTasks.addAll(taskManager.sortTasksByDeadlineDesc());
                break;
            case "По возрастанию приоритета":
                sortedTasks.addAll(taskManager.sortTasksByPriorityAsc());
                break;
            case "По убыванию приоритета":
                sortedTasks.addAll(taskManager.sortTasksByPriorityDesc());
                break;
        }

        tasksTable.setItems(sortedTasks);
    }

    @FXML
    private void handleLogout() {
        userManager.logout();
        showLoginScreen();
    }

    @FXML
    private void handleExit() {
        userManager.saveToFile();
        stage.close();
    }

    private void refreshTasks() {
        tasks = FXCollections.observableArrayList(taskManager.getTasks());
        tasksTable.setItems(tasks);
        updateTaskCount();
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        datePicker.setValue(LocalDate.now());
        hourComboBox.setValue(LocalTime.now().getHour());
        minuteComboBox.setValue(LocalTime.now().getMinute());
        priorityComboBox.setValue(null);
    }

    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/taskmanagerfx/login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setUserManager(userManager);
            loginController.setStage(stage);

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.setTitle("To-Do List System - Вход");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить экран входа");
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
module com.example.taskmanagerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jdk.jfr;

    opens com.example.taskmanagerfx to javafx.fxml;
    opens com.example.taskmanagerfx.model to com.google.gson, javafx.base;
    opens com.example.taskmanagerfx.controllers to javafx.fxml;
    opens com.example.taskmanagerfx.service to com.google.gson;

    exports com.example.taskmanagerfx;
    exports com.example.taskmanagerfx.controllers;
    exports com.example.taskmanagerfx.model;
    exports com.example.taskmanagerfx.service;
}
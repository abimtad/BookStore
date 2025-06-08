package com.bookstore.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminDashboardController {
    @FXML
    private Label messageLabel;

    @FXML
    private void handleManageBooks() {
        System.out.println("Manage Books button clicked");
        try {
            var resource = getClass().getResource("/fxml/book_management.fxml");
            System.out.println("book_management.fxml resource: " + resource);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error loading book management: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageUsers() {
        // TODO: Implement user management
        messageLabel.setText("User management coming soon!");
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error during logout");
        }
    }
} 
package com.bookstore.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerDashboardController {
    @FXML
    private Label messageLabel;

    @FXML
    private void handleBrowseBooks() {
        // TODO: Implement book browsing
        messageLabel.setText("Book browsing coming soon!");
    }

    @FXML
    private void handleViewOrders() {
        // TODO: Implement order viewing
        messageLabel.setText("Order history coming soon!");
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
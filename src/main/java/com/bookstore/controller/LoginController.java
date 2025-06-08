package com.bookstore.controller;

import com.bookstore.model.User;
import com.bookstore.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label messageLabel;
    
    private final UserService userService = new UserService();
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        User user = userService.authenticate(username, password);
        if (user != null) {
            // Load appropriate view based on user role
            String fxmlFile = user.isAdmin() ? "/fxml/admin_dashboard.fxml" : "/fxml/customer_dashboard.fxml";
            loadView(fxmlFile);
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error loading registration form");
            e.printStackTrace();
        }
    }
    
    private void loadView(String fxmlFile) {
        try {
            System.out.println("Attempting to load view: " + fxmlFile);
            URL resourceUrl = LoginController.class.getResource(fxmlFile);
            if (resourceUrl == null) {
                throw new RuntimeException("Could not find FXML file: " + fxmlFile);
            }
            System.out.println("Found FXML file at: " + resourceUrl);
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error loading view: " + e.getMessage());
            e.printStackTrace();
            messageLabel.setText("Error loading view: " + e.getMessage());
        }
    }
} 
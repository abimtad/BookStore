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
        System.out.println("Login button clicked");
        String username = usernameField.getText();
        String password = passwordField.getText();
        System.out.println("Attempting login for username: " + username);
        try {
            User user = userService.authenticate(username, password);
            if (user != null) {
                System.out.println("Login successful for user: " + user.getUsername());
                loadView(user.isAdmin() ? "/fxml/admin_dashboard.fxml" : "/fxml/customer_dashboard.fxml");
            } else {
                messageLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            messageLabel.setText("Error during login: " + e.getMessage());
            e.printStackTrace();
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
        System.out.println("Loading view: " + fxmlFile);
        try {
            var resource = getClass().getResource(fxmlFile);
            System.out.println("FXML resource: " + resource);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error loading view: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
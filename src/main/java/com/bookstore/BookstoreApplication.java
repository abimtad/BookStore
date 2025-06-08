package com.bookstore;

import com.bookstore.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookstoreApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            System.out.println("Initializing database...");
            DatabaseUtil.initializeDatabase();
            System.out.println("Database initialized successfully");
            
            // Load the login view
            System.out.println("Loading login view...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            if (loader.getLocation() == null) {
                throw new RuntimeException("Could not find login.fxml");
            }
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            primaryStage.setTitle("Bookstore Login");
            primaryStage.setScene(scene);
            primaryStage.show();
            System.out.println("Application started successfully");
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    @Override
    public void stop() {
        try {
            // Close database connection when application closes
            DatabaseUtil.closeConnection();
            System.out.println("Database connection closed");
        } catch (Exception e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 
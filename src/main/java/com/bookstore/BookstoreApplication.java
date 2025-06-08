package com.bookstore;

import com.bookstore.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookstoreApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database
        DatabaseUtil.initializeDatabase();
        
        // Load the login view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        primaryStage.setTitle("Bookstore Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop() {
        // Close database connection when application closes
        DatabaseUtil.closeConnection();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 
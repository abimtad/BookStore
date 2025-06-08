package com.bookstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root_password"; // Updated password for root user
    
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // First, connect without database to create it if it doesn't exist
                String baseUrl = "jdbc:mysql://localhost:3306";
                Connection tempConn = DriverManager.getConnection(baseUrl, USER, PASSWORD);
                try (Statement stmt = tempConn.createStatement()) {
                    stmt.execute("CREATE DATABASE IF NOT EXISTS bookstore");
                }
                tempConn.close();

                // Now connect to the bookstore database
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Successfully connected to database");
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            System.out.println("Initializing database...");
            
            // Create tables if they don't exist
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    is_admin BOOLEAN DEFAULT FALSE
                )
            """;
            
            String createBooksTable = """
                CREATE TABLE IF NOT EXISTS books (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    title VARCHAR(255) NOT NULL,
                    author VARCHAR(255) NOT NULL,
                    price DECIMAL(10,2) NOT NULL,
                    stock INT NOT NULL,
                    description TEXT
                )
            """;
            
            String createOrdersTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    user_id BIGINT NOT NULL,
                    order_date DATETIME NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            
            String createOrderItemsTable = """
                CREATE TABLE IF NOT EXISTS order_items (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    order_id BIGINT NOT NULL,
                    book_id BIGINT NOT NULL,
                    quantity INT NOT NULL,
                    price DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(id),
                    FOREIGN KEY (book_id) REFERENCES books(id)
                )
            """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createUsersTable);
                stmt.execute(createBooksTable);
                stmt.execute(createOrdersTable);
                stmt.execute(createOrderItemsTable);
                System.out.println("Tables created successfully");
                
                // Insert admin user if not exists
                String insertAdmin = """
                    INSERT IGNORE INTO users (username, password, email, is_admin)
                    VALUES ('admin', 'admin123', 'admin@bookstore.com', TRUE)
                """;
                stmt.execute(insertAdmin);
                System.out.println("Admin user created/verified");
            }
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
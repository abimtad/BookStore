package com.bookstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Create tables if they don't exist
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    is_admin BOOLEAN DEFAULT FALSE
                )
            """;
            
            String createBooksTable = """
                CREATE TABLE IF NOT EXISTS books (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    title VARCHAR(255) NOT NULL,
                    author VARCHAR(255) NOT NULL,
                    price DECIMAL(10,2) NOT NULL,
                    stock INT NOT NULL,
                    description TEXT
                )
            """;
            
            String createOrdersTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT NOT NULL,
                    order_date DATETIME NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """;
            
            String createOrderItemsTable = """
                CREATE TABLE IF NOT EXISTS order_items (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    order_id INT NOT NULL,
                    book_id INT NOT NULL,
                    quantity INT NOT NULL,
                    price DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(id),
                    FOREIGN KEY (book_id) REFERENCES books(id)
                )
            """;
            
            conn.createStatement().execute(createUsersTable);
            conn.createStatement().execute(createBooksTable);
            conn.createStatement().execute(createOrdersTable);
            conn.createStatement().execute(createOrderItemsTable);
            
            // Insert admin user if not exists
            String insertAdmin = """
                INSERT IGNORE INTO users (username, password, email, is_admin)
                VALUES ('admin', 'admin123', 'admin@bookstore.com', TRUE)
            """;
            conn.createStatement().execute(insertAdmin);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
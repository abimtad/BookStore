package com.bookstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root_password"; // Updated password for root user
    
    private static Connection connection;
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    
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
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
    
    public static void initializeDatabase() {
        try {
            // Create database if it doesn't exist
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE DATABASE IF NOT EXISTS bookstore");
                System.out.println("Database created or already exists");
            }

            // Create tables
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                Statement stmt = conn.createStatement();
                
                // Create users table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        is_admin BOOLEAN DEFAULT FALSE
                    )
                """);
                System.out.println("Users table created or already exists");
                
                // Create books table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS books (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        author VARCHAR(255) NOT NULL,
                        isbn VARCHAR(13) UNIQUE,
                        price DECIMAL(10,2) NOT NULL,
                        stock INT NOT NULL,
                        description TEXT
                    )
                """);
                System.out.println("Books table created or already exists");
                
                // Create orders table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        order_date DATETIME NOT NULL,
                        shipping_address TEXT,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                """);
                System.out.println("Orders table created or already exists");
                
                // Create order_items table
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS order_items (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        order_id BIGINT NOT NULL,
                        book_id BIGINT NOT NULL,
                        quantity INT NOT NULL,
                        price DECIMAL(10,2) NOT NULL,
                        FOREIGN KEY (order_id) REFERENCES orders(id),
                        FOREIGN KEY (book_id) REFERENCES books(id)
                    )
                """);
                System.out.println("Order items table created or already exists");
                
                // Create admin user if it doesn't exist
                stmt.execute("""
                    INSERT IGNORE INTO users (username, password, email, is_admin)
                    VALUES ('admin', 'admin123', 'admin@bookstore.com', TRUE)
                """);
                System.out.println("Admin user created/verified");
            }
            
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null || !entityManager.isOpen()) {
            if (entityManagerFactory == null) {
                Map<String, String> properties = new HashMap<>();
                properties.put("jakarta.persistence.jdbc.url", URL);
                properties.put("jakarta.persistence.jdbc.user", USER);
                properties.put("jakarta.persistence.jdbc.password", PASSWORD);
                properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
                properties.put("hibernate.hbm2ddl.auto", "update");
                properties.put("hibernate.show_sql", "true");
                
                entityManagerFactory = Persistence.createEntityManagerFactory("bookstore", properties);
            }
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }
} 
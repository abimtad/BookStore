package com.bookstore.service;

import com.bookstore.model.User;
import com.bookstore.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    
    public User authenticate(String username, String password) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                
                System.out.println("Attempting to authenticate user: " + username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setAdmin(rs.getBoolean("is_admin"));
                    System.out.println("User authenticated successfully: " + username);
                    return user;
                }
                System.out.println("Authentication failed for user: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean register(User user) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // First check if username or email already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, user.getUsername());
                checkStmt.setString(2, user.getEmail());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Username or email already exists: " + user.getUsername());
                    return false;
                }
            }
            
            // If not exists, proceed with registration
            String insertQuery = "INSERT INTO users (username, password, email, is_admin) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, user.getUsername());
                insertStmt.setString(2, user.getPassword());
                insertStmt.setString(3, user.getEmail());
                insertStmt.setBoolean(4, user.isAdmin());
                
                int result = insertStmt.executeUpdate();
                System.out.println("User registration " + (result > 0 ? "successful" : "failed") + " for: " + user.getUsername());
                return result > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 
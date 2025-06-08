package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class BookFormController {
    @FXML
    private TextField titleField;
    
    @FXML
    private TextField authorField;
    
    @FXML
    private TextField isbnField;
    
    @FXML
    private TextField priceField;
    
    @FXML
    private TextField stockField;
    
    @FXML
    private TextArea descriptionField;
    
    @FXML
    private TextField publicationYearField;
    
    @FXML
    private TextField categoriesField;
    
    @FXML
    private TextField coverImageUrlField;
    
    @FXML
    private Label messageLabel;
    
    private Book book;
    private BookManagementController bookManagementController;
    private final BookService bookService = new BookService();
    
    public void setBook(Book book) {
        this.book = book;
        populateFields();
    }
    
    public void setBookManagementController(BookManagementController controller) {
        this.bookManagementController = controller;
    }
    
    private void populateFields() {
        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            priceField.setText(book.getPrice().toString());
            stockField.setText(book.getStock().toString());
            descriptionField.setText(book.getDescription());
            publicationYearField.setText(book.getPublicationYear() != null ? book.getPublicationYear().toString() : "");
            categoriesField.setText(String.join(", ", book.getCategories()));
            coverImageUrlField.setText(book.getCoverImageUrl());
        }
    }
    
    @FXML
    private void handleSave() {
        try {
            if (book == null) {
                book = new Book();
            }
            
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setIsbn(isbnField.getText());
            book.setPrice(new BigDecimal(priceField.getText()));
            book.setStock(Integer.parseInt(stockField.getText()));
            book.setDescription(descriptionField.getText());
            
            String yearText = publicationYearField.getText();
            if (!yearText.isEmpty()) {
                book.setPublicationYear(Integer.parseInt(yearText));
            }
            
            String categoriesText = categoriesField.getText();
            if (!categoriesText.isEmpty()) {
                Set<String> categories = new HashSet<>();
                for (String category : categoriesText.split(",")) {
                    categories.add(category.trim());
                }
                book.setCategories(categories);
            }
            
            book.setCoverImageUrl(coverImageUrlField.getText());
            
            if (book.getId() == null) {
                bookService.addBook(book);
            } else {
                bookService.updateBook(book);
            }
            
            bookManagementController.refreshBooks();
            closeWindow();
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter valid numbers for price, stock, and publication year");
        } catch (Exception e) {
            messageLabel.setText("Error saving book: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancel() {
        closeWindow();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
} 
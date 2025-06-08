package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.math.BigDecimal;

public class BookManagementController {
    @FXML
    private TableView<Book> bookTable;
    
    @FXML
    private TableColumn<Book, Long> idColumn;
    
    @FXML
    private TableColumn<Book, String> titleColumn;
    
    @FXML
    private TableColumn<Book, String> authorColumn;
    
    @FXML
    private TableColumn<Book, BigDecimal> priceColumn;
    
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    
    @FXML
    private TableColumn<Book, Void> actionsColumn;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private TableColumn<Book, String> isbnColumn;
    
    @FXML
    private TableColumn<Book, Integer> yearColumn;
    
    @FXML
    private TextField searchField;
    
    private final BookService bookService = new BookService();
    
    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        // Set up actions column
        actionsColumn.setCellFactory(createButtonCell());
        
        // Load books
        refreshBooks();
    }
    
    private Callback<TableColumn<Book, Void>, TableCell<Book, Void>> createButtonCell() {
        return new Callback<>() {
            @Override
            public TableCell<Book, Void> call(TableColumn<Book, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    
                    {
                        editButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            handleEditBook(book);
                        });
                        
                        deleteButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            handleDeleteBook(book);
                        });
                    }
                    
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(5, editButton, deleteButton);
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };
    }
    
    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
            Parent root = loader.load();
            BookFormController controller = loader.getController();
            controller.setBookManagementController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Add New Book");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            messageLabel.setText("Error opening book form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleEditBook(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
            Parent root = loader.load();
            BookFormController controller = loader.getController();
            controller.setBookManagementController(this);
            controller.setBook(book);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            messageLabel.setText("Error opening book form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleDeleteBook(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Book");
        alert.setContentText("Are you sure you want to delete this book?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    bookService.deleteBook(book.getId());
                    refreshBooks();
                    messageLabel.setText("Book deleted successfully");
                } catch (Exception e) {
                    messageLabel.setText("Error deleting book: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    
    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) bookTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            messageLabel.setText("Error returning to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshBooks();
        } else {
            try {
                bookTable.getItems().setAll(bookService.searchBooks(query));
            } catch (Exception e) {
                messageLabel.setText("Error searching books: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public void refreshBooks() {
        try {
            bookTable.getItems().setAll(bookService.getAllBooks());
        } catch (Exception e) {
            messageLabel.setText("Error loading books: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
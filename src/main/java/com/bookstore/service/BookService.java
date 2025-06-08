package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.util.DatabaseUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class BookService {
    private final EntityManager entityManager;

    public BookService() {
        this.entityManager = DatabaseUtil.getEntityManager();
    }

    public Book addBook(Book book) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(book);
            entityManager.getTransaction().commit();
            return book;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error adding book: " + e.getMessage(), e);
        }
    }

    public List<Book> getAllBooks() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);
        return query.getResultList();
    }

    public Book getBookById(Long id) {
        return entityManager.find(Book.class, id);
    }

    public Book updateBook(Book book) {
        try {
            entityManager.getTransaction().begin();
            Book updatedBook = entityManager.merge(book);
            entityManager.getTransaction().commit();
            return updatedBook;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating book: " + e.getMessage(), e);
        }
    }

    public void deleteBook(Long id) {
        try {
            entityManager.getTransaction().begin();
            Book book = entityManager.find(Book.class, id);
            if (book != null) {
                entityManager.remove(book);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting book: " + e.getMessage(), e);
        }
    }

    public List<Book> searchBooks(String query) {
        TypedQuery<Book> typedQuery = entityManager.createQuery(
            "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:query) " +
            "OR LOWER(b.author) LIKE LOWER(:query) " +
            "OR LOWER(b.isbn) LIKE LOWER(:query)", Book.class);
        typedQuery.setParameter("query", "%" + query + "%");
        return typedQuery.getResultList();
    }
} 
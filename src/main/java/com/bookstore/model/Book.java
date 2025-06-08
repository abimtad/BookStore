package com.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @ElementCollection
    @CollectionTable(name = "book_categories", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    private Set<String> categories = new HashSet<>();

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Version
    private Long version;
} 
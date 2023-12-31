package com.bbacks.bst.domain.books.domain;

import com.bbacks.bst.domain.categories.domain.Category;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "book_info")
@Getter
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id")
    private Long bookId;

    @Column(name = "b_isbn13")
    private String bookISBN13;

    @Column(name = "b_name")
    private String bookTitle;

    @Column(name = "b_author")
    private String bookAuthor;

    @Column(name = "b_picture")
    private String bookImg;

    @Column(name = "b_publisher")
    private String bookPublisher;

    @Column(name = "b_year")
    private LocalDate bookPubYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id")
    private Category bookCategory;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
//    private List<Debate> debate = new ArrayList<>();

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
//    private List<Review> review = new ArrayList<>();

}

package com.bbacks.bst.categories.domain;

import com.bbacks.bst.books.domain.Book;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_category")
@Getter
public class Category {
    @Id @GeneratedValue
    @Column(name = "c_id")
    private Long categoryId;

    @Column(name = "c_name")
    private String categoryName;

    @OneToMany(mappedBy = "bookCategory")
    private List<Book> books = new ArrayList<>();

}

package com.bbacks.bst.books.dto;


import com.bbacks.bst.books.domain.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetailResponse {

    private String bookTitle;
    private String bookImg;
    private String bookAuthor;
    private String bookPublisher;

    public static BookDetailResponse of(Book book) {
        return BookDetailResponse.builder()
                .bookTitle(book.getBookTitle())
                .bookImg(book.getBookImg())
                .bookAuthor(book.getBookAuthor())
                .bookPublisher(book.getBookPublisher())
                .build();
    }
}

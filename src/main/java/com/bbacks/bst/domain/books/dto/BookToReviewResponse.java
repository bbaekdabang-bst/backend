package com.bbacks.bst.domain.books.dto;

import com.bbacks.bst.domain.books.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookToReviewResponse {
    private Long bookId;
    private String bookTitle;
    private String bookImg;
    private String bookAuthor;
    private String bookPublisher;

    public static BookToReviewResponse from(Book book) {
        return BookToReviewResponse.builder()
                .bookId(book.getBookId())
                .bookTitle(book.getBookTitle())
                .bookImg(book.getBookImg())
                .bookAuthor(book.getBookAuthor())
                .bookPublisher(book.getBookPublisher())
                .build();
    }
}

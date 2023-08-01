package com.bbacks.bst.books.repository;

import com.bbacks.bst.books.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookImgAndId> findTop10ByOrderByBookIdAsc();
    List<BookImgAndId> findTop10ByOrderByBookPubYearDesc();
    List<BookImgAndId> findAllByBookTitleContainingOrBookAuthorContainingOrBookPublisherContainingOrderByBookIdAsc(String keyword1, String keyword2, String keyword3);
    BookDetail findByBookId(Long bookId);

    List<BookToReview> findAllByBookTitleContainingOrBookAuthorContainingOrderByBookIdAsc(String keyword1, String keyword2);

}

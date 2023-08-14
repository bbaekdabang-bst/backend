package com.bbacks.bst.domain.books.repository;

import com.bbacks.bst.domain.books.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookImgAndId> findTop10ByOrderByBookIdAsc();
    List<BookImgAndId> findTop10ByOrderByBookPubYearDesc();
    List<BookImgAndId> findAllByBookTitleContainingOrBookAuthorContainingOrBookPublisherContainingOrderByBookIdAsc(String keyword1, String keyword2, String keyword3);
    Optional<Book> findByBookId(Long bookId);
    List<Book> findAllByBookTitleContainingOrBookAuthorContainingOrderByBookIdAsc(String keyword1, String keyword2);

}

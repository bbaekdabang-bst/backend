package com.bbacks.bst.books.repository;

import com.bbacks.bst.books.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookImgAndId> findTop10ByOrderByBookIdAsc();
    List<BookImgAndId> findTop10ByOrderByBookPubYearDesc();
    List<BookImgAndId> findAllByBookTitleContainingOrBookAuthorContainingOrBookPublisherContainingOrderByBookIdAsc(String keyword1, String keyword2, String keyword3);
    Optional<Book> findByBookId(Long bookId);

    List<BookToReview> findAllByBookTitleContainingOrBookAuthorContainingOrderByBookIdAsc(String keyword1, String keyword2);

}

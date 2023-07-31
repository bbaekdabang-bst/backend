package com.bbacks.bst.books.repository;

import com.bbacks.bst.books.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookImgAndId> findTop10ByOrderByBookIdAsc();
    List<BookImgAndId> findTop10ByOrderByBookPubYearDesc();
    List<BookImgAndId> findAllByBookTitleContainingOrBookAuthorContainingOrderByBookIdAsc(String keyword1, String keyword2);

    BookDetail findByBookId(Long bookId);

}

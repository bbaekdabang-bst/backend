package com.bbacks.bst.domain.books.repository;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.categories.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<BookImgAndId> findTop10ByOrderByBookIdAsc();
    List<BookImgAndId> findTop10ByOrderByBookPubYearDesc();
    Optional<Book> findByBookId(Long bookId);
    @Query("SELECT b FROM Book b WHERE " +
            "(:keyword1 IS NOT NULL AND b.bookTitle LIKE %:keyword1%) OR " +
            "(:keyword2 IS NOT NULL AND b.bookAuthor LIKE %:keyword2%) OR " +
            "(:keyword3 IS NOT NULL AND b.bookPublisher LIKE %:keyword3%) " +
            "ORDER BY b.bookId ASC")
    List<Book> searchBooksByKeywords(
            @Param("keyword1") String keyword1,
            @Param("keyword2") String keyword2,
            @Param("keyword3") String keyword3
    );

    List<Book> findByBookTitleContainingOrBookAuthorContaining(String keyword, String keyword1);

    List<Book> findByBookCategory(Category category);
}

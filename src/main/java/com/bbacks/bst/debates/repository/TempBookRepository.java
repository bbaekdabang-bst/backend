package com.bbacks.bst.debates.repository;

import com.bbacks.bst.books.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempBookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBookTitleContainingOrBookAuthorContaining(String titleKeyword, String authorKeyword);
}

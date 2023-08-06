package com.bbacks.bst.debates.repository;

import com.bbacks.bst.books.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempBookRepository extends JpaRepository<Book, Long> {
}

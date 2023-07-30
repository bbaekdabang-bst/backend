package com.bbacks.bst.books;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookImgAndId> findAllBooks(){
//        return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "bookId"));
        return bookRepository.findTop10ByOrderByBookIdAsc();

    }
}

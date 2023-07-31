package com.bbacks.bst.books.controller;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.books.dto.BookMainResponse;
import com.bbacks.bst.books.repository.BookDetail;
import com.bbacks.bst.books.repository.BookImgAndId;
import com.bbacks.bst.books.service.BookService;
import com.bbacks.bst.common.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ResultResponse> getMainBooks(){

        BookMainResponse bookMainResponse = bookService.getMainBooks();
        return ResponseEntity.ok().body(ResultResponse.from(bookMainResponse));

    }

    @GetMapping("/search")
    public ResponseEntity<ResultResponse> searchBookByKeyword(@RequestParam String keyword) {

        List<BookImgAndId> keywordContainingBooks = bookService.getBooksByKeyword(keyword);
        return ResponseEntity.ok().body(ResultResponse.from(keywordContainingBooks));
    }

    @GetMapping("/detail/{bookId}")
    public ResponseEntity<ResultResponse> getBookDetail(@PathVariable Long bookId){
        BookDetail bookDetail = bookService.getBookDetail(bookId);
        return ResponseEntity.ok().body(ResultResponse.from(bookDetail));
    }


}

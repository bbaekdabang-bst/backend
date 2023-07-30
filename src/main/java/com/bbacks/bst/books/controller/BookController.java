package com.bbacks.bst.books;

import com.bbacks.bst.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public ResponseEntity<ResultResponse> getAllBooks(){
        List<BookImgAndId> books = bookService.findAllBooks();
        return ResponseEntity.ok().body(ResultResponse.from(books));

    }



}

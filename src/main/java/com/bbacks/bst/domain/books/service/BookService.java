package com.bbacks.bst.domain.books.service;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.books.dto.BookDetailResponse;
import com.bbacks.bst.domain.books.dto.BookMainResponse;
import com.bbacks.bst.domain.books.dto.BookToReviewResponse;
import com.bbacks.bst.domain.books.repository.BookImgAndId;
import com.bbacks.bst.domain.books.repository.BookRepository;
import com.bbacks.bst.global.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookMainResponse getMainBooks(){
        /**
         * 의문. 만약 Top10, Recent10이 아니라 다른 조건으로 조회할 데이터가 변경된다면?
         *      현재는 bookRepository에 메소드 하나 추가하고, 이 부분을 변경해줘야 한다.
         */
        List<BookImgAndId> bookImgAndIdList1 = bookRepository.findTop10ByOrderByBookIdAsc();
        List<BookImgAndId> bookImgAndIdList2 = bookRepository.findTop10ByOrderByBookPubYearDesc();

        return BookMainResponse.of(bookImgAndIdList1, bookImgAndIdList2);

    }

    @Transactional(readOnly = true)
    public List<BookImgAndId> getBooksByKeyword(String keyword){
        List<Book> books = bookRepository.searchBooksByKeywords(keyword, keyword, keyword);
        return books.stream()
                .map(book -> new BookImgAndId() {
                    @Override
                    public Long getBookId() {
                        return book.getBookId();
                    }

                    @Override
                    public String getBookImg() {
                        return book.getBookImg();
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookDetailResponse getBookDetail(Long bookId) {
        Book book = bookRepository.findByBookId(bookId).orElseThrow(PostNotFoundException::new);
        BookDetailResponse bookDetailResponse = BookDetailResponse.of(book);
        return bookDetailResponse;
    }

    @Transactional(readOnly = true)
    public List<BookToReviewResponse> searchBookToReview(String keyword) {
        List<Book> books = bookRepository.searchBooksByKeywords(keyword, keyword, null);
        return books.stream()
                .map(BookToReviewResponse::from)
                .collect(Collectors.toList());
    }

}

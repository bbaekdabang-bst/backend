package com.bbacks.bst.domain.books.controller;


import com.bbacks.bst.domain.books.dto.BookDetailResponse;
import com.bbacks.bst.domain.books.dto.BookMainResponse;
import com.bbacks.bst.domain.debates.dto.DebateInBookDetailResponse;
import com.bbacks.bst.domain.debates.service.DebateService;
import com.bbacks.bst.domain.reviews.dto.ReviewInBookDetailResponse;
import com.bbacks.bst.domain.reviews.service.ReviewService;
import com.bbacks.bst.domain.books.repository.BookImgAndId;
import com.bbacks.bst.domain.books.service.BookService;
import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import com.querydsl.core.Tuple;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final DebateService debateService;

    @GetMapping
    public ApiResponseDto<?> getMainBooks(){
        BookMainResponse bookMainResponse = bookService.getMainBooks();
//        return ResponseEntity.ok().body(ResultResponse.from(bookMainResponse));
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookMainResponse);
    }

    @GetMapping("/search")
    public ApiResponseDto<?> searchBookByKeyword(@Parameter(name = "keyword", in = ParameterIn.QUERY) @RequestParam @NotBlank String keyword) {
        List<BookImgAndId> keywordContainingBooks = bookService.getBooksByKeyword(keyword);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, keywordContainingBooks);
    }

    @GetMapping("/detail/{bookId}")
    public ApiResponseDto<?> getBookDetail(@Parameter(name = "bookId", in = ParameterIn.PATH) @PathVariable Long bookId){
        BookDetailResponse bookDetailResponse = bookService.getBookDetail(bookId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookDetailResponse);
    }

//    // offset, limit 사용하여 페이징 처리
//    @GetMapping("/detail/{bookId}/review2")
//    public ApiResponseDto<?> getBookDetailReviewWithOffset(@Parameter(name = "bookId", in = ParameterIn.PATH) @PathVariable Long bookId,
//                                                 Pageable pageable) {
//        long startTime = System.currentTimeMillis();
//
//        List<ReviewInBookDetailResponse> bookDetailReview = reviewService.getBookDetailReview(bookId, pageable).getContent();
//
//
//        long stopTime = System.currentTimeMillis();
//        System.out.println("코드 실행 시간:"+(stopTime - startTime));
//
//        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookDetailReview);
//    }

    @GetMapping("/detail/{bookId}/review")
    public ApiResponseDto<?> getBookDetailReviewNoOffset(@Parameter(name = "bookId", in = ParameterIn.PATH) @PathVariable Long bookId,
                                                         @RequestParam(value = "last", required = false) Long reviewId){
        List<ReviewInBookDetailResponse> bookDetailReview = reviewService.getBookDetailReviewNoOffset(bookId, reviewId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookDetailReview);
    }

    @GetMapping("/detail/{bookId}/debate")
    public ApiResponseDto<?> getBookDetailDebateNoOffset(@Parameter(name = "bookId", in = ParameterIn.PATH) @PathVariable Long bookId,
                                                         @RequestParam(value = "last", required = false) Long debateId){

        List<DebateInBookDetailResponse> bookDetailDebate = debateService.getBookDetailDebateNoOffset(bookId, debateId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookDetailDebate);
    }

}

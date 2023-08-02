package com.bbacks.bst.books.controller;


import com.bbacks.bst.books.dto.BookDetailResponse;
import com.bbacks.bst.books.dto.BookMainResponse;
import com.bbacks.bst.books.dto.BookToReviewResponse;
import com.bbacks.bst.reviews.dto.ReviewDetailResponse;
import com.bbacks.bst.reviews.dto.ReviewInBookDetailResponse;
import com.bbacks.bst.reviews.dto.ReviewRequest;
import com.bbacks.bst.books.repository.BookImgAndId;
import com.bbacks.bst.books.service.BookService;
import com.bbacks.bst.reviews.service.ReviewService;
import com.bbacks.bst.common.response.ApiResponseDto;
import com.bbacks.bst.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;

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
//        List<BookDetailReviewResponse> bookDetailReviewResponseList = reviewService.getBookDetailReview(bookId, pageable).getContent();
//
//
//        long stopTime = System.currentTimeMillis();
//        System.out.println("코드 실행 시간:"+(stopTime - startTime));
//
//        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookDetailReviewResponseList);
//    }

    @GetMapping("/detail/{bookId}/review")
    public ApiResponseDto<?> getBookDetailReviewNoOffset(@Parameter(name = "bookId", in = ParameterIn.PATH) @PathVariable Long bookId,
                                                         @RequestParam(value = "last", required = false) Long reviewId){
        List<ReviewInBookDetailResponse> bookDetailReview = reviewService.getBookDetailReviewNoOffset(bookId, reviewId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookDetailReview);
    }

    @GetMapping("/review/search")
    public ApiResponseDto<?> searchBookToReview(@RequestParam @NotBlank String keyword) {
        List<BookToReviewResponse> bookToReview = bookService.searchBookToReview(keyword);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookToReview);
    }

    @GetMapping("/review/{reviewId}")
    public ApiResponseDto<?> getBookReviewDetail(@Parameter(name = "reviewId", in= ParameterIn.PATH) @PathVariable Long reviewId){
        ReviewDetailResponse reviewDetailResponse = reviewService.getReviewDetail(reviewId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, reviewDetailResponse);
    }

    @GetMapping("/review/{reviewId}/bookmark")
    public ApiResponseDto<?> bookmarkReview(@Parameter(name = "reviewId", in= ParameterIn.PATH) @PathVariable Long reviewId){
        /**
         * 임시 userId
         */
        Long userId = 1L;
        Long responseStatus = reviewService.bookmarkReview(userId, reviewId);
        if(responseStatus==0){
            return ApiResponseDto.success(SuccessStatus.BOOKMARK_DELETE_SUCCESS);
        }
        return ApiResponseDto.success(SuccessStatus.BOOKMARK_SAVE_SUCCESS);
    }


    @PostMapping(value = "/review/{bookId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponseDto<?> postBookReview(@Parameter(name = "bookId", in= ParameterIn.PATH) @PathVariable Long bookId,
                                            @RequestPart(value = "body") @Valid ReviewRequest reviewRequest,
                                            @RequestPart(value = "file", required = false) MultipartFile file){
        /**
         * 임시 userId
         */
        Long userId = 1L;

        Long reviewId = reviewService.postBookReview(bookId, userId, reviewRequest, file);
        return ApiResponseDto.success(SuccessStatus.POST_SUCCESS);

    }



}

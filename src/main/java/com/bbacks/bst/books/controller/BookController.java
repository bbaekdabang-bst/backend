package com.bbacks.bst.books.controller;


import com.bbacks.bst.books.dto.BookMainResponse;
import com.bbacks.bst.books.repository.BookDetail;
import com.bbacks.bst.books.repository.BookImgAndId;
import com.bbacks.bst.books.repository.BookToReview;
import com.bbacks.bst.books.repository.ReviewDetail;
import com.bbacks.bst.books.service.BookService;
import com.bbacks.bst.books.service.ReviewService;
import com.bbacks.bst.common.response.ApiResponseDto;
import com.bbacks.bst.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponseDto<?> getBookDetail(@Parameter(name = "bookId", in= ParameterIn.PATH) @PathVariable Long bookId){
        BookDetail bookDetail = bookService.getBookDetail(bookId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookDetail);
    }

    @GetMapping("/review/search")
    public ApiResponseDto<?> searchBookToReview(@RequestParam @NotBlank String keyword) {
        List<BookToReview> bookToReview = bookService.searchBookToReview(keyword);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookToReview);
    }

    @GetMapping("/review/{reviewId}")
    public ApiResponseDto<?> getBookReviewDetail(@Parameter(name = "reviewId", in= ParameterIn.PATH) @PathVariable Long reviewId){
        List<ReviewDetail> reviewDetails = reviewService.getReviewDetail(reviewId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, reviewDetails);
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




//    @PostMapping("/review/{bookId}")
//    public ApiResponseDto<?> postBookReview(){
//        /**
//         * bookId -> @PathVariable
//         * 독후감 제목
//         * 독후감 내용
//         * 독후감 사진
//         * 스포일러 유무
//         * 비공개 유무
//         * 유저 id
//         */
//
//    }

}

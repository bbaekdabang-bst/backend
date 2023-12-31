package com.bbacks.bst.domain.reviews.controller;

import com.bbacks.bst.domain.books.dto.BookToReviewResponse;
import com.bbacks.bst.domain.books.service.BookService;
import com.bbacks.bst.domain.reviews.dto.ReviewDetailCommentResponse;
import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import com.bbacks.bst.domain.reviews.dto.ReviewCommentRequest;
import com.bbacks.bst.domain.reviews.dto.ReviewDetailResponse;
import com.bbacks.bst.domain.reviews.dto.ReviewRequest;
import com.bbacks.bst.domain.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final BookService bookService;
    private final ReviewService reviewService;

    private Long getUserId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    @GetMapping("/search")
    public ApiResponseDto<?> searchBookToReview(@Parameter(name = "keyword", in = ParameterIn.QUERY) @RequestParam @NotBlank String keyword) {
        List<BookToReviewResponse> bookToReview = bookService.searchBookToReview(keyword);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bookToReview);
    }

    @GetMapping("/{reviewId}")
    public ApiResponseDto<?> getReviewDetail(@Parameter(name = "reviewId", in= ParameterIn.PATH) @PathVariable Long reviewId,
                                             Authentication authentication){
        ReviewDetailResponse reviewDetailResponse = reviewService.getReviewDetail(reviewId, getUserId(authentication));
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, reviewDetailResponse);
    }

    @GetMapping("/{reviewId}/comment")
    public ApiResponseDto<?> getReviewDetailComments(@Parameter(name = "reviewId", in= ParameterIn.PATH) @PathVariable Long reviewId){
        List<ReviewDetailCommentResponse> responses = reviewService.getReviewDetailComment(reviewId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, responses);
    }

    @GetMapping("/{reviewId}/bookmark")
    public ApiResponseDto<?> putReviewBookMark(@Parameter(name = "reviewId", in= ParameterIn.PATH) @PathVariable Long reviewId,
                                            Authentication authentication){
        Long userId = getUserId(authentication);

        Long responseStatus = reviewService.bookmarkReview(userId, reviewId);
        if(responseStatus==0){
            return ApiResponseDto.success(SuccessStatus.BOOKMARK_DELETE_SUCCESS);
        }
        return ApiResponseDto.success(SuccessStatus.BOOKMARK_SAVE_SUCCESS);
    }


//    @PostMapping(value = "/{bookId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ApiResponseDto<?> postBookReview(@Parameter(name = "bookId", in= ParameterIn.PATH) @PathVariable Long bookId,
//                                            @RequestPart(value = "body") @Valid ReviewRequest reviewRequest,
//                                            @RequestPart(value = "file", required = false) MultipartFile file,
//                                            Authentication authentication){
//
//        Long userId = getUserId(authentication);
//
//        Long reviewId = reviewService.postBookReview(bookId, userId, reviewRequest, file);
//        return ApiResponseDto.success(SuccessStatus.POST_SUCCESS);
//
//    }

    @PostMapping(value = "/{bookId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponseDto<?> postBookReview(@Parameter(name = "bookId", in= ParameterIn.PATH) @PathVariable Long bookId,
                                            @Valid @ModelAttribute ReviewRequest reviewRequest,
                                            Authentication authentication){

        Long userId = getUserId(authentication);
        Long reviewId = reviewService.postBookReview(bookId, userId, reviewRequest);
        return ApiResponseDto.success(SuccessStatus.POST_SUCCESS, reviewId);

    }

    @PostMapping("/{reviewId}/comment")
    public ApiResponseDto<?> bookReviewComment(@Parameter(name = "reviewId", in= ParameterIn.PATH) @PathVariable Long reviewId,
                                               @RequestBody ReviewCommentRequest commentRequest,
                                               Authentication authentication){
        Long userId = Long.parseLong(authentication.getName());
        Long commentId = reviewService.postReviewComment(reviewId, userId, commentRequest);
        return ApiResponseDto.success(SuccessStatus.COMMENT_SUCCESS);
    }



}

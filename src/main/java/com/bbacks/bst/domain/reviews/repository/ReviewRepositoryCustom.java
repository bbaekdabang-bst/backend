package com.bbacks.bst.domain.reviews.repository;

import com.bbacks.bst.domain.reviews.dto.ReviewDetailCommentResponse;
import com.bbacks.bst.domain.reviews.dto.ReviewDetailResponse;
import com.bbacks.bst.domain.reviews.dto.ReviewInBookDetailResponse;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<ReviewInBookDetailResponse> findReviewByBookIdNoOffset(Long bookId, Long reviewId);
    ReviewDetailResponse findReviewById(Long reviewId);
    List<ReviewDetailCommentResponse> findReviewCommentById(Long reviewId);

}

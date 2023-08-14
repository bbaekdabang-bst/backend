package com.bbacks.bst.domain.reviews.dto;

import com.bbacks.bst.domain.reviews.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewInBookDetailResponse {
    private String reviewTitle;
    private String reviewContent;
    private Long reviewId;
    private String reviewerNickname;

    public static ReviewInBookDetailResponse from(Review review) {
        return ReviewInBookDetailResponse.builder()
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .reviewId(review.getReviewId())
                .reviewerNickname(review.getUser().getUserNickname())
                .build();
    }
}

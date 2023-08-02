package com.bbacks.bst.reviews.dto;

import com.bbacks.bst.reviews.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookDetailReviewResponse {
    private String reviewTitle;
    private String reviewContent;
    private Long reviewId;
    private String reviewerNickname;

    public static BookDetailReviewResponse from(Review review) {
        return BookDetailReviewResponse.builder()
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .reviewId(review.getReviewId())
                .reviewerNickname(review.getUser().getUserNickname())
                .build();
    }
}

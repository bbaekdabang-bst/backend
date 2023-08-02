package com.bbacks.bst.reviews.dto;

import com.bbacks.bst.reviews.domain.Review;
import com.bbacks.bst.reviews.domain.ReviewComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReviewDetailResponse {
    private String reviewTitle;
    private String reviewContent;
    private String reviewImg;
    private String reviewerNickname;
    private String reviewerPhoto;
    private List<ReviewComment> reviewComments;

    public static ReviewDetailResponse from(Review review){
        return ReviewDetailResponse.builder()
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .reviewImg(review.getReviewImg())
                .reviewerNickname(review.getUser().getUserNickname())
                .reviewComments(review.getReviewComment())
                .build();
    }
}

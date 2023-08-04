package com.bbacks.bst.reviews.dto;

import com.bbacks.bst.reviews.domain.Review;
import com.bbacks.bst.reviews.domain.ReviewComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
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
    private List<ReviewCommentResponse> reviewComments;

    public static ReviewDetailResponse from(Review review){
        ReviewDetailResponseBuilder builder = ReviewDetailResponse.builder()
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .reviewImg(review.getReviewImg())
                .reviewerNickname(review.getUser().getUserNickname());

        List<ReviewCommentResponse> reviewCommentResponses = new ArrayList<>();
        for (ReviewComment reviewComment : review.getReviewComment()) {
            ReviewCommentResponse commentResponse = ReviewCommentResponse.builder()
                    .commenterId(reviewComment.getUser().getUserId())
                    .commenterNickname(reviewComment.getUser().getUserNickname())
                    .commenterImg(reviewComment.getUser().getUserPhoto())
                    .commentText(reviewComment.getCommentText())
                    .build();
            reviewCommentResponses.add(commentResponse);
        }
        builder.reviewComments(reviewCommentResponses);
        return builder.build();
    }

}

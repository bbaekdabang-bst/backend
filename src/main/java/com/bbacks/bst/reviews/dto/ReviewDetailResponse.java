package com.bbacks.bst.reviews.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewDetailResponse {
    private String reviewTitle;
    private String reviewContent;
    private String reviewImg;
    private String reviewerNickname;
    private String reviewerPhoto;
    private List<ReviewDetailCommentResponse> reviewComments;

    public void setReviewComments(List<ReviewDetailCommentResponse> reviewComments) {
        this.reviewComments = reviewComments;
    }

    @QueryProjection
    public ReviewDetailResponse(String reviewTitle, String reviewContent, String reviewImg,
                                   String reviewerNickname, String reviewerPhoto){
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.reviewImg = reviewImg;
        this.reviewerNickname = reviewerNickname;
        this.reviewerPhoto = reviewerPhoto;
    }
}

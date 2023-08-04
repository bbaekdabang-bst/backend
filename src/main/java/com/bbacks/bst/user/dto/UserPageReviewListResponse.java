package com.bbacks.bst.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPageReviewListResponse {
    private Long reviewId;
    private String reviewTitle;
    private Long bookId;
    private String bookTitle;
}

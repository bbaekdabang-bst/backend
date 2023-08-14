package com.bbacks.bst.domain.user.dto;

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

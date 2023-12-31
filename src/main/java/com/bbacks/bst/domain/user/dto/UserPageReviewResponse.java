package com.bbacks.bst.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPageReviewResponse {
    private Long reviewId;
    private Long bookId;
    private String bookImg;
}

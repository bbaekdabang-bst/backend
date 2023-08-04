package com.bbacks.bst.reviews.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class ReviewCommentResponse {
    private Long commenterId;
    private String commenterNickname;
    private String commenterImg;
    private String commentText;
}

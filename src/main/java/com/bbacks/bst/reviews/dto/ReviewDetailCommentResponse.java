package com.bbacks.bst.reviews.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ReviewDetailCommentResponse {
    private Long commenterId;
    private String commenterNickname;
    private String commenterImg;
    private String commentText;

    @QueryProjection
    public ReviewDetailCommentResponse(Long commenterId, String commenterNickname,
                                          String commenterImg, String commentText){
        this.commenterId = commenterId;
        this.commenterNickname = commenterNickname;
        this.commenterImg = commenterImg;
        this.commentText = commentText;
    }
}

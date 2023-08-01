package com.bbacks.bst.books.repository;

import com.bbacks.bst.books.domain.ReviewComment;

import java.util.List;

public interface ReviewDetail {
    String getReviewTitle();
    String getReviewContent();
    String getReviewImg();
    UserDto getUser();
    List<ReviewComment> getReviewComment();

    interface UserDto {
        String getUserNickname();
        String getUserPhoto();
    }

    interface ReviewCommentDto {
        Long getCommentId();
        String getCommentText();
        UserDto getUser();
    }
}

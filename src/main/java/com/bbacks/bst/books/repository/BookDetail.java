package com.bbacks.bst.books.repository;

import java.util.List;

public interface BookDetail {
    String getBookTitle();
    String getBookImg();
    String getBookAuthor();
    String getBookPublisher();
    List<BookReviewDto> getReview();
    List<BookDebateDto> getDebate();

    interface BookReviewDto {
        String getReviewTitle();
        ReviewUser getUser();
        String getReviewContent();
        Long getReviewId();
    }

    interface BookDebateDto {
        Long getDebateId();
        String getDebateTopic();
        Integer getDebateType();
        Long getDebatePostCount();
    }

    interface ReviewUser {
        String getUserNickname();
    }

}

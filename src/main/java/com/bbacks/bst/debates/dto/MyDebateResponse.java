package com.bbacks.bst.debates.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyDebateResponse {
    private String bookTitle;
    private String bookAuthor;
    private String debateTopic;
    private Integer debateType;
    private String categoryName;

    @Builder
    public MyDebateResponse(String bookTitle, String bookAuthor, String debateTopic, Integer debateType, String categoryName) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.debateTopic = debateTopic;
        this.debateType = debateType;
        this.categoryName = categoryName;
    }
}

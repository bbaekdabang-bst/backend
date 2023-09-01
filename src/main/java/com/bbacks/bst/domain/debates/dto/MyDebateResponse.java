package com.bbacks.bst.domain.debates.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyDebateResponse {
    private String bookTitle;
    private String bookAuthor;
    private Long debateId;
    private String debateTopic;
    private Integer debateType;
    private String categoryName;

    @Builder
    public MyDebateResponse(String bookTitle, String bookAuthor, Long debateId, String debateTopic, Integer debateType, String categoryName) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.debateId = debateId;
        this.debateTopic = debateTopic;
        this.debateType = debateType;
        this.categoryName = categoryName;
    }
}

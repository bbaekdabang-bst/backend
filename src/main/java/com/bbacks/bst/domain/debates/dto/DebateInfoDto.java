package com.bbacks.bst.domain.debates.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DebateInfoDto {
    private String bookTitle;
    private String bookAuthor;
    private String topic;

    @Builder
    public DebateInfoDto(String bookTitle, String bookAuthor, String topic) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.topic = topic;
    }
}

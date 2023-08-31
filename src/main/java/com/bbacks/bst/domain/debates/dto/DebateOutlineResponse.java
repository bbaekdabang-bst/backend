package com.bbacks.bst.domain.debates.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

// 주제, 책 제목, 저자, 토론 설명, 타입, ㅊ마여자 수, 비밀방 여부

@Getter
public class DebateOutlineResponse {
    private String debateTopic;
    private Integer debateType;
    private String bookTitle;
    private String bookAuthor;
    private String category;
    private String debateDescription;
    private Integer debateParticipants;
    private Date debateCreatedAt;
    private Integer isPrivate;

    @Builder
    public DebateOutlineResponse(String debateTopic, Integer debateType, String bookTitle, String bookAuthor, String cateogory, String debateDescription, Integer debateParticipants, Date debateCreatedAt, Integer isPrivate) {
        this.debateTopic = debateTopic;
        this.debateType = debateType;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.category = cateogory;
        this.debateDescription = debateDescription;
        this.debateParticipants = debateParticipants;
        this.debateCreatedAt = debateCreatedAt;
        this.isPrivate = isPrivate;
    }
}

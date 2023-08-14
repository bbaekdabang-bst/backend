package com.bbacks.bst.domain.debates.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class DebateOutlineResponse {
    private String debateTopic;
    private Integer debateType;
    private String userNickname;
    private String userPhoto;
    private Integer debateParticipants;
    private Date debateCreatedAt;
    private Integer isPrivate;

    @Builder
    public DebateOutlineResponse(String debateTopic, Integer debateType, Integer debateParticipants, Date debateCreatedAt, Integer isPrivate) {
        this.debateTopic = debateTopic;
        this.debateType = debateType;
        this.debateParticipants = debateParticipants;
        this.debateCreatedAt = debateCreatedAt;
        this.isPrivate = isPrivate;
    }
}

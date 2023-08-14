package com.bbacks.bst.domain.debates.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateDebateRequest {
    private Long bookId;
    private String debateTopic;
    private int debateType;
    private String debateDescription;
    private String debatePassword;
}

package com.bbacks.bst.domain.debates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DebateInBookDetailResponse {

    private String debateTopic;
    private Long debateId;
    private Integer debateType;
    private Integer debatePostCount;

}

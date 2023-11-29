package com.bbacks.bst.domain.debates.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateDebateRequest {
    @NotNull(message = "책 지정은 필수입니다.")
    private Long bookId;
    @NotBlank(message = "토론 주제를 입력해주세요.")
    private String debateTopic;
    private int debateType;
    private String debateDescription;
    private String debatePassword;
}

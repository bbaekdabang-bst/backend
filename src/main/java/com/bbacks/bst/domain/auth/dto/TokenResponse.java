package com.bbacks.bst.domain.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshKey;
}

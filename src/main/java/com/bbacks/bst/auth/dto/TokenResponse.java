package com.bbacks.bst.auth.dto;

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

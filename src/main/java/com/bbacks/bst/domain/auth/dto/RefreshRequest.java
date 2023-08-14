package com.bbacks.bst.domain.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank
    private String refreshKey;
}

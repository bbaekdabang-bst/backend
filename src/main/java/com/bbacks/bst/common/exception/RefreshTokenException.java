package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ErrorStatus;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException() {
        super(ErrorStatus.INVALID_REFRESH_TOKEN_EXCEPTION.getMessage());
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}

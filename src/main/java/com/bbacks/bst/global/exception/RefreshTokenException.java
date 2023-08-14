package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException() {
        super(ErrorStatus.INVALID_REFRESH_TOKEN_EXCEPTION.getMessage());
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}

package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ErrorStatus;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(ErrorStatus.INVALID_ACCESS_TOKEN_EXCEPTION.getMessage());
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}

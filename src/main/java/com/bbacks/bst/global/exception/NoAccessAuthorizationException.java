package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class NoAccessAuthorizationException extends RuntimeException {

    public NoAccessAuthorizationException() {
        super(ErrorStatus.NO_ACCESS_EXCEPTION.getMessage());
    }

    public NoAccessAuthorizationException(String message) {
        super(message);
    }
}

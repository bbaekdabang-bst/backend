package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ErrorStatus;

public class NoAccessAuthorizationException extends RuntimeException {

    public NoAccessAuthorizationException() {
        super(ErrorStatus.NO_ACCESS_EXCEPTION.getMessage());
    }

    public NoAccessAuthorizationException(String message) {
        super(message);
    }
}

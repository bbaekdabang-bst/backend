package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class NeedLoginException extends RuntimeException {

    public NeedLoginException() {
        super(ErrorStatus.NEED_LOGIN_EXCEPTION.getMessage());
    }

    public NeedLoginException(String message) {
        super(message);
    }
}

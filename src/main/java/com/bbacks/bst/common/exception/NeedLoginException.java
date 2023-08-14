package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ErrorStatus;

public class NeedLoginException extends RuntimeException {

    public NeedLoginException() {
        super(ErrorStatus.NEED_LOGIN_EXCEPTION.getMessage());
    }

    public NeedLoginException(String message) {
        super(message);
    }
}

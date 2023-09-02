package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class UserNotFoundInRedisException extends RuntimeException {

    public UserNotFoundInRedisException() {
        super(ErrorStatus.NEED_LOGIN_EXCEPTION.getMessage());
    }
}

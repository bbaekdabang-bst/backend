package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ErrorStatus;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage());
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}

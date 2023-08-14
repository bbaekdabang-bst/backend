package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage());
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}

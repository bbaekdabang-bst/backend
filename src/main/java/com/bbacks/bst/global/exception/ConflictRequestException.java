package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException() {
        super(ErrorStatus.ALREADY_EXIST_RESOURCE_EXCEPTION.getMessage());
    }

    public ConflictRequestException(String message) {
        super(message);
    }
}

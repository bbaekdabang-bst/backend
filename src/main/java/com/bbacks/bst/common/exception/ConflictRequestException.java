package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ErrorStatus;

public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException() {
        super(ErrorStatus.ALREADY_EXIST_RESOURCE_EXCEPTION.getMessage());
    }

    public ConflictRequestException(String message) {
        super(message);
    }
}

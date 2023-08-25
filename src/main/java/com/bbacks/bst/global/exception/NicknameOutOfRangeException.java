package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class NicknameOutOfRangeException extends RuntimeException {
    public NicknameOutOfRangeException() {
        super(ErrorStatus.NICKNAME_INDEX_OUT_OF_RANGE_EXCEPTION.getMessage());
    }
}

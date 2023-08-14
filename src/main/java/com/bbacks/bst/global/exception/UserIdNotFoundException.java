package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ErrorStatus;

public class UserIdNotFoundException extends RuntimeException{
    public UserIdNotFoundException(){
        super(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage());
    }
}

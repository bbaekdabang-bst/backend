package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ErrorStatus;

public class UserIdNotFoundException extends RuntimeException{
    public UserIdNotFoundException(){
        super(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage());
    }
}

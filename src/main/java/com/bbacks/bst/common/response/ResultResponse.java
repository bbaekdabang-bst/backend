package com.bbacks.bst.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultResponse {
    private Integer statusCode;
    private String message;
    private Object data;

    private ResultResponse(Object data){
        this.data = data;
    }

    public static ResultResponse from(Object data) {
        return new ResultResponse(data);
    }
}

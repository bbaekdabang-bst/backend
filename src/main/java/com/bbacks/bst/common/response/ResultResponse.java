package com.bbacks.bst.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultResponse {
    private String message;
    private Object data;

    private ResultResponse(String message) {
        this.message = message;
    }

    private ResultResponse(Object data) {
        this.data = data;
    }

    public static ResultResponse from(String message) {
        return new ResultResponse(message);
    }

    public static ResultResponse from(Object data) {
        return new ResultResponse(data);
    }
}

package com.bbacks.bst.common.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseDto<T> {
    private final int statusCode;
    private final String message;
    private T data;

    public static ApiResponseDto success(SuccessStatus successStatus) {
        return new ApiResponseDto<>(successStatus.getHttpStatusCode(), successStatus.getMessage());
    }

    public static <T> ApiResponseDto<T> success(SuccessStatus successStatus, T data) {
        return new ApiResponseDto<T>(successStatus.getHttpStatusCode(), successStatus.getMessage(), data);
    }


    public static ApiResponseDto error(HttpStatus httpStatus, String message) {
        return new ApiResponseDto<>(httpStatus.value(), message);
    }

    public static <T> ApiResponseDto<T> error(HttpStatus httpStatus, String message, T data) {
        return new ApiResponseDto<T>(httpStatus.value(), message, data);
    }
}

package com.bbacks.bst.global.exception;

import com.bbacks.bst.global.response.ApiResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class})
    protected ApiResponseDto handleMethodArgumentNotValidException(Exception e){
//        log.warn("MethodArgumentNotValid", e);
        return ApiResponseDto.error(HttpStatus.BAD_REQUEST, "Input Validation Error");
//        return ApiResponseDto.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentTypeMismatchException.class,
            MaxUploadSizeExceededException.class
    })
    protected ApiResponseDto handleBadRequestException(Exception e) {
//        log.warn("BAD_REQUEST", e);
        return ApiResponseDto.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
            UserIdNotFoundException.class,
            NeedLoginException.class,
            NoAccessAuthorizationException.class,
            RefreshTokenException.class,
    })
    public ApiResponseDto handleUnauthorizedException(Exception e) {
//        log.warn("UNAUTHORIZED", e);
        return ApiResponseDto.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    protected ApiResponseDto handleNotFoundException(PostNotFoundException e) {
//        log.warn("NOT_FOUND", e);
        return ApiResponseDto.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
            ConflictRequestException.class,
    })
    protected ApiResponseDto handleConflictRequestException(ConflictRequestException e) {
//        log.warn("CONFLICT", e);
        return ApiResponseDto.error(HttpStatus.CONFLICT, e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ApiResponseDto handleException(Exception e) {
//        log.error("INTERNAL_SERVER_ERROR", e);
        return ApiResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }




}

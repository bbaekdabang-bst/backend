package com.bbacks.bst.common.exception;

import com.bbacks.bst.common.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResultResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.warn("MethodArgumentNotValid", e);
        return ResultResponse.from(
                Optional.of(e.getBindingResult())
                        .map(Errors::getFieldError)
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .orElseGet(e::getMessage)
        );
    }




}

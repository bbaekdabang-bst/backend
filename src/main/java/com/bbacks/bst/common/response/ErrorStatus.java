package com.bbacks.bst.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

    /**
     * 400 BAD REQUEST
     */
    REQUEST_VALIDATION_EXCEPTION("Validation을 통과하지 못했습니다."),
    NO_REQUEST_PARAMETER_EXCEPTION("요청 파라미터 값이 없습니다."),
    VALIDATION_WRONG_TYPE_EXCEPTION("잘못된 타입이 입력되었습니다."),
    PARAMETER_TYPE_MISMATCH_EXCEPTION("파라미터의 타입이 잘못됐습니다."),
    INVALID_PASSWORD_EXCEPTION("잘못된 비밀번호가 입력됐습니다."),
    INVALID_DATE_EXCEPTION("날짜의 형태는 yyyy-mm-dd 형태여야합니다."),
    INVALID_TIME_EXCEPTION("시간의 형태는 HH:mm:00의 형태여야합니다."),
    NULL_ACCESS_TOKEN_EXCEPTION("토큰 값이 없습니다."),

    /**
     * 401 UNAUTHORIZED
     */
    JWT_EXCEPTION("JWT 토큰을 확인하십시오."),
    TOKEN_TIME_EXPIRED_EXCEPTION("만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN_EXCEPTION("유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN_EXCEPTION("유효하지 않은 엑세스 토큰입니다."),
    NO_ACCESS_EXCEPTION("해당 리소스에 접근 권한이 없습니다."),
    NEED_LOGIN_EXCEPTION("로그인이 필요합니다."),
//    INVALID_ROLE_EXCEPTION(HttpStatus.UNAUTHORIZED,"유효하지 않은 역할의 유저입니다."),



    /**
     * 404 NOT FOUND
     */
    USER_NOT_FOUND_EXCEPTION("존재하지 않는 유저입니다"),
    POST_NOT_FOUND_EXCEPTION("존재하지 않는 포스트입니다."),
    NOT_ALLOWED_NOTIFICATION_EXCEPTION("푸쉬알림 상대가 알림을 허용하지 않았습니다."),



    /**
     * 409 CONFLICT
     */
    ALREADY_EXIST_USER_EXCEPTION("이미 존재하는 유저입니다."),
    ALREADY_EXIST_RESOURCE_EXCEPTION("이미 존재하는 리소스입니다."),


    /**
     * 500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR("알 수 없는 서버 에러가 발생했습니다");

    private final String message;

}

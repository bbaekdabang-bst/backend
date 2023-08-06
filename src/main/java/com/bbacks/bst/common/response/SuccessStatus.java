package com.bbacks.bst.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessStatus {
    GET_SUCCESS(HttpStatus.OK, "조회에 성공했습니다."),
    POST_SUCCESS(HttpStatus.CREATED, "독후감 작성에 성공했습니다."),
    COMMENT_SUCCESS(HttpStatus.CREATED, "댓글 작성에 성공했습니다."),
    BOOKMARK_SAVE_SUCCESS(HttpStatus.OK, "북마크되었습니다."),
    BOOKMARK_DELETE_SUCCESS(HttpStatus.OK, "북마크해제되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공했습니다.");


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode(){
        return httpStatus.value();
    }

}
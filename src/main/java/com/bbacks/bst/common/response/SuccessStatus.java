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
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공했습니다."),
    DEBATE_POST_SUCCESS(HttpStatus.OK, "글 작성에 성공했습니다."),
    DELETE_POST_SUCCESS(HttpStatus.OK, "글이 삭제되었습니다."),
    CREATE_DEBATE_SUCCESS(HttpStatus.OK, "토론이 개설되었습니다."),
    JOIN_DEBATE_SUCCESS(HttpStatus.OK, "토론 참여에 성공했습니다."),
    LIKE_POST_SUCCESS(HttpStatus.OK, "좋아요에 성공하였습니다."),
    UNLIKE_POST_SUCCESS(HttpStatus.OK, "좋아요를 취소하였습니다."),
    DISLIKE_POST_SUCCESS(HttpStatus.OK, "싫어요에 성공하였습니다."),
    UNDISLIKE_POST_SUCCESS(HttpStatus.OK, "싫어요를 취소하였습니다.");



    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode(){
        return httpStatus.value();
    }

}
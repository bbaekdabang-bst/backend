package com.bbacks.bst.debates.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
public class QuotationDto {
    // 작성자 닉네임, 프사, 작성일, 글 본문
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String userNickname;
    private String userPhoto;
    private String content;

    @Builder
    public QuotationDto(Date date, String userNickname, String userPhoto, String content) {
        this.date = date;
        this.userNickname = userNickname;
        this.userPhoto = userPhoto;
        this.content = content;
    }
}

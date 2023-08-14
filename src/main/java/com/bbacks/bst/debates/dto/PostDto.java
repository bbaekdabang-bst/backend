package com.bbacks.bst.debates.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
public class PostDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String userNickname;
    private String userPhoto;
    private String content;
    private Long quotedPostId;
    private Integer like;
    private Integer dislike;
    private Integer isPro;

    @Builder
    public PostDto(Date date, String userNickname, String userPhoto, String content, Long quotedPostId, Integer like, Integer dislike, Integer isPro) {
        this.date = date;
        this.userNickname = userNickname;
        this.userPhoto = userPhoto;
        this.content = content;
        this.quotedPostId = quotedPostId;
        this.like = like;
        this.dislike = dislike;
        this.isPro = isPro;
    }
}

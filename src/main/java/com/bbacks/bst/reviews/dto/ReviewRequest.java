package com.bbacks.bst.reviews.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ReviewRequest {

    @NotNull(message = "독후감 제목은 필수입니다.")
    private String reviewTitle;

    @NotNull(message = "독후감 내용은 필수입니다.")
    private String reviewContent;

    private Boolean reviewSpoiler = false;

    private Boolean reviewPrivate = false;


}

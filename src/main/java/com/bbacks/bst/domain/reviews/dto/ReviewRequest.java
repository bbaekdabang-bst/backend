package com.bbacks.bst.domain.reviews.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


//@Data
//public class ReviewRequest {
//
//    @NotNull(message = "독후감 제목은 필수입니다.")
//    private String reviewTitle;
//
//    @NotNull(message = "독후감 내용은 필수입니다.")
//    private String reviewContent;
//
//    private Boolean reviewSpoiler = false;
//
//    private Boolean reviewPrivate = false;
//
//
//}

@Builder
@Data
@AllArgsConstructor
public class ReviewRequest {
    @NotNull(message = "독후감 제목은 필수입니다.")
    private String reviewTitle;

    @NotNull(message = "독후감 내용은 필수입니다.")
    private String reviewContent;

    private Boolean reviewSpoiler = false;

    private Boolean reviewPrivate = false;

    @Nullable
    private MultipartFile file;
}
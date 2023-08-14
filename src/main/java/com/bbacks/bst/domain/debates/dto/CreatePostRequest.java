package com.bbacks.bst.domain.debates.dto;

import lombok.Getter;

@Getter
public class CreatePostRequest {
    private Long debateId;
    private Long userId;
    private String postContent;
    private String postPhoto;
    private Long postQuotationId;
    private Integer postIsPro;

}

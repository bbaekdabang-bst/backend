package com.bbacks.bst.domain.debates.dto;

import lombok.Getter;

@Getter
public class CreatePostRequest {
//    private Long debateId;
    private String postContent;
    private String postPhoto;
    private Long postQuotationId;
    private Integer postIsPro;

}

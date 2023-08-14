package com.bbacks.bst.debates.dto;

import com.bbacks.bst.debates.domain.Debate;
import com.bbacks.bst.debates.domain.Post;
import lombok.Builder;
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

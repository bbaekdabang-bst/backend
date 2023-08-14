package com.bbacks.bst.domain.debates.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReadPostResponse {
    private DebateInfoDto debateInfoDto;
    private PostDto postDto;
    private List<QuotationDto> quotationDtos;

    @Builder
    public ReadPostResponse(DebateInfoDto debateInfoDto, PostDto postDto, List<QuotationDto> quotationDtos) {
        this.debateInfoDto = debateInfoDto;
        this.postDto = postDto;
        this.quotationDtos = quotationDtos;
    }
}

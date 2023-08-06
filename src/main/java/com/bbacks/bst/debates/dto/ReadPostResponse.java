package com.bbacks.bst.debates.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
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

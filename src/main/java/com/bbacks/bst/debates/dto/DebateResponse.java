package com.bbacks.bst.debates.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DebateResponse {
    private boolean nextPage;
    private List<PostDto> posts;
}

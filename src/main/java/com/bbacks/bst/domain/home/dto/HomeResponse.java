package com.bbacks.bst.domain.home.dto;

import com.bbacks.bst.domain.debates.dto.MyDebateResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HomeResponse {
    private List<MyDebateResponse> bestDebates;
    private List<MyDebateResponse> newDebates;

    @Builder
    public HomeResponse(List<MyDebateResponse> bestDebates, List<MyDebateResponse> newDebates) {
        this.bestDebates = bestDebates;
        this.newDebates = newDebates;
    }

}
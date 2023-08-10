package com.bbacks.bst.debates.controller;

import com.bbacks.bst.common.response.ApiResponseDto;
import com.bbacks.bst.common.response.SuccessStatus;
import com.bbacks.bst.debates.dto.CreateDebateRequest;
import com.bbacks.bst.debates.dto.DebateOutlineResponse;
import com.bbacks.bst.debates.dto.MyDebateResponse;
import com.bbacks.bst.debates.dto.PostDto;
import com.bbacks.bst.debates.service.DebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DebateController {

    private final DebateService debateService;

    // 내가 참여한 토론
    @GetMapping("/mydebate")
    public ApiResponseDto<?> myDebate(@RequestParam("user-id") Long userId) {
        List<MyDebateResponse> myDebateResponses = debateService.myDebate(userId);

        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, myDebateResponses);
    }

    // 토론 개설
    @PostMapping("/debate/create")
    public ApiResponseDto<?> createDebate(@RequestBody CreateDebateRequest createDebateRequest) {
        debateService.createDebate(createDebateRequest);

        return ApiResponseDto.success(SuccessStatus.CREATE_DEBATE_SUCCESS);
    }

    // 토론 개요 페이지
    @GetMapping("/debate/{deb-id}/outline")
    public ApiResponseDto<?> debateOutline(@PathVariable("deb-id") Long debateId) {
        DebateOutlineResponse debateOutlineResponse = debateService.debateOutline(debateId);

        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, debateOutlineResponse);
    }

    // 토론방 참여하기
    @PutMapping("/debate/{deb-id}/join")
    public ApiResponseDto<?> join (@PathVariable("deb-id") Long debateId, @RequestParam("user-id") Long userId) {
        debateService.join(debateId, userId);

        return ApiResponseDto.success(SuccessStatus.JOIN_DEBATE_SUCCESS);
    }

    // 토론방 피드
    @GetMapping("/debate/feed/{deb-id}")
    public List<PostDto> debateFeed (@PathVariable("deb-id") Long debateId) {
        List<PostDto> posts = debateService.debateFeed(debateId);

        return posts;
    }
}

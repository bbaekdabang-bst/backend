package com.bbacks.bst.debates.controller;

import com.bbacks.bst.debates.domain.Post;
import com.bbacks.bst.debates.dto.CreateDebateRequest;
import com.bbacks.bst.debates.dto.DebateOutlineResponse;
import com.bbacks.bst.debates.dto.MyDebateResponse;
import com.bbacks.bst.debates.dto.PostDto;
import com.bbacks.bst.debates.service.DebateService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DebateController {

    private final DebateService debateService;

    // 내가 참여한 토론
    @GetMapping("/mydebate")
    public List<MyDebateResponse> myDebate(@RequestParam("user-id") Long userId) {
        List<MyDebateResponse> myDebateResponses = debateService.myDebate(userId);

        return myDebateResponses;
    }

    // 토론 개설
    @PostMapping("/debate/create")
    public Long createDebate(@RequestBody CreateDebateRequest createDebateRequest) {
        Long result = debateService.createDebate(createDebateRequest);

        return result;
    }

    // 토론 개요 페이지
    @GetMapping("/debate/{deb-id}/outline")
    public DebateOutlineResponse debateOutline(@PathVariable("deb-id") Long debateId) {
        DebateOutlineResponse debateOutlineResponse = debateService.debateOutline(debateId);

        return debateOutlineResponse;
    }

    // 토론방 참여하기
    @PutMapping("/debate/{deb-id}/join")
    public int join (@PathVariable("deb-id") Long debateId, @RequestParam("user-id") Long userId) {
        int result = debateService.join(debateId, userId);

        return result;
    }


    // 토론방 피드
    @GetMapping("/debate/feed/{deb-id}")
    public List<PostDto> debateFeed (@PathVariable("deb-id") Long debateId) {
        List<PostDto> posts = debateService.debateFeed(debateId);

        return posts;
    }
}

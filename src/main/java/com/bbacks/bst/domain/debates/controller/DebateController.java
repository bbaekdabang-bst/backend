package com.bbacks.bst.domain.debates.controller;

import com.amazonaws.Response;
import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import com.bbacks.bst.domain.debates.dto.CreateDebateRequest;
import com.bbacks.bst.domain.debates.dto.DebateOutlineResponse;
import com.bbacks.bst.domain.debates.dto.MyDebateResponse;
import com.bbacks.bst.domain.debates.dto.PostDto;
import com.bbacks.bst.domain.debates.service.DebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DebateController {

    private final DebateService debateService;

    private Long getUserId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    // 내가 참여한 토론
    @GetMapping("/mydebate")
    public ApiResponseDto<?> myDebate(Authentication authentication) {
        Long userId = getUserId(authentication);
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
    public ResponseEntity<?> join (@PathVariable("deb-id") Long debateId, Authentication authentication,
                                @RequestBody(required = false) Map<String, String> passwordMap) {
        Long userId = getUserId(authentication);
        int result = debateService.join(debateId, userId, passwordMap.get("password"));

        HttpHeaders headers = new HttpHeaders();
        if (result == 0) {
            headers.setLocation(URI.create("/debate/"+debateId+"/outline"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
        headers.setLocation(URI.create("/debate/feed/"+debateId));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
    /*@PutMapping("/debate/{deb-id}/join")
    public ApiResponseDto<?> join (@PathVariable("deb-id") Long debateId, Authentication authentication) {
        Long userId = getUserId(authentication);
        debateService.join(debateId, userId);

        return ApiResponseDto.success(SuccessStatus.JOIN_DEBATE_SUCCESS);
    }*/


    // 토론방 피드
    @GetMapping("/debate/feed/{deb-id}")
    public ApiResponseDto<?> debateFeed (@PathVariable("deb-id") Long debateId) {
        List<PostDto> posts = debateService.debateFeed(debateId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, posts);
    }
}

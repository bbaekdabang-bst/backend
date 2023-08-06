package com.bbacks.bst.debates.controller;

import com.bbacks.bst.common.response.ApiResponseDto;
import com.bbacks.bst.common.response.SuccessStatus;
import com.bbacks.bst.debates.dto.CreatePostRequest;
import com.bbacks.bst.debates.dto.ReadPostResponse;
import com.bbacks.bst.debates.service.PostService;
import lombok.ToString;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 글 작성
    @PostMapping("/debate/feed/{deb-id}/create")
    public ApiResponseDto<?> createPost(@PathVariable("deb-id") Long debateId, @RequestBody CreatePostRequest createPostRequest) {
        postService.createPost(createPostRequest);

        return ApiResponseDto.success(SuccessStatus.DEBATE_POST_SUCCESS);
    }

    // 글 상세 페이지
    @GetMapping("/debate/post/{deb-id}/{post-id}")
    public ApiResponseDto<?> readPost(@PathVariable("deb-id") Long debateId, @PathVariable("post-id") Long postId) {
        ReadPostResponse readPostResponse = postService.readPost(postId);

        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, readPostResponse);
    }

    // 글 삭제
    @PutMapping("/debate/post/{p-id}/delete")
    public ApiResponseDto<?> deletePost(@PathVariable("p-id") Long postId) {
        postService.deletePost(postId);

        return ApiResponseDto.success(SuccessStatus.DELETE_POST_SUCCESS);
    }

}
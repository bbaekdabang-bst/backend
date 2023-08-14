package com.bbacks.bst.domain.debates.controller;

import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import com.bbacks.bst.domain.debates.dto.CreatePostRequest;
import com.bbacks.bst.domain.debates.dto.ReadPostResponse;
import com.bbacks.bst.domain.debates.service.PostService;
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

    // 글 좋아요
    @PostMapping("/debate/post/{post-id}/like")
    public ApiResponseDto<?> likePost(@PathVariable("post-id") Long postId, @RequestParam("user-id") Long userId) {
        boolean liked = postService.likePost(userId, postId);

        String result = null;
        if (liked) {
             return ApiResponseDto.success(SuccessStatus.LIKE_POST_SUCCESS);
        } else {
            return ApiResponseDto.success(SuccessStatus.UNLIKE_POST_SUCCESS);
        }
    }

    // 글 싫어요
    @PostMapping("/debate/post/{post-id}/dislike")
    public ApiResponseDto<?> dislikePost(@PathVariable("post-id") Long postId, @RequestParam("user-id") Long userId) {
        boolean disliked = postService.dislikePost(userId, postId);
        if (disliked) {
            return ApiResponseDto.success(SuccessStatus.DISLIKE_POST_SUCCESS);
        } else {
            return ApiResponseDto.success(SuccessStatus.UNDISLIKE_POST_SUCCESS);
        }
    }

    // 글 북마크
    @PostMapping("/debate/post/{post-id}/bookmark")
    public ApiResponseDto<?> bookmarkPost(@PathVariable("post-id") Long postId, @RequestParam("user-id") Long userId) {
        boolean bookmark = postService.bookmark(userId, postId);
        if(bookmark) {
            return ApiResponseDto.success(SuccessStatus.BOOKMARK_SAVE_SUCCESS);
        } else {
            return ApiResponseDto.success(SuccessStatus.BOOKMARK_DELETE_SUCCESS);
        }
    }

}

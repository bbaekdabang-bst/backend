package com.bbacks.bst.debates.service;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.debates.domain.Debate;
import com.bbacks.bst.debates.domain.Post;
import com.bbacks.bst.debates.domain.PostBookmark;
import com.bbacks.bst.debates.dto.*;
import com.bbacks.bst.debates.repository.DebateRepository;
import com.bbacks.bst.debates.repository.PostBookmarkRepository;
import com.bbacks.bst.debates.repository.PostRepository;
import com.bbacks.bst.user.domain.User;
import com.bbacks.bst.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final DebateRepository debateRepository;
    private final UserRepository userRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // 글 작성
    @Transactional
    public void createPost(CreatePostRequest createPostRequest) {
        Debate debate = debateRepository.findById(createPostRequest.getDebateId()).get();
        User user = userRepository.findById(createPostRequest.getUserId()).get();

        Post post = Post.builder()
                .debate(debate)
                .user(user)
                .postContent(createPostRequest.getPostContent())
                .postPhoto(createPostRequest.getPostPhoto())
                .postQuotationId(createPostRequest.getPostQuotationId())
                .postIsPro(createPostRequest.getPostIsPro())
                .build();

        postRepository.save(post);
    }

    // 글 상세 페이지
    public ReadPostResponse readPost(Long postId) {
        Post post = postRepository.findById(postId).get();
        Debate debate = post.getDebate();
        Book book = debate.getBookId();
        User user = post.getUser();

        List<Post> posts = postRepository.findByPostQuotationId(post.getPostId());
        List<QuotationDto> quotationDtos = new ArrayList<>();

        if(!posts.isEmpty()) {
            for(Post p:posts) {
                QuotationDto quotationDto = readQuotation(p, user);
                quotationDtos.add(quotationDto);
            }
        }

        DebateInfoDto debateInfoDto = DebateInfoDto.builder()
                .bookTitle(book.getBookTitle())
                .bookAuthor(book.getBookAuthor())
                .topic(debate.getDebateTopic())
                .build();

        PostDto postDto = PostDto.builder()
                .date(post.getPostCreatedAt())
                .userNickname(user.getUserNickname())
                .userPhoto(user.getUserPhoto())
                .content(post.getPostContent())
                .quotedPostId(post.getPostQuotationId())
                .like(getLikeCount(postId))
                .dislike(getDislikeCount(postId))
                .isPro(post.getPostIsPro())
                .build();

        ReadPostResponse readPostResponse = ReadPostResponse.builder()
                .debateInfoDto(debateInfoDto)
                .postDto(postDto)
                .quotationDtos(quotationDtos)
                .build();

        return readPostResponse;
    }
    public QuotationDto readQuotation(Post post, User user) {

        QuotationDto quotationDto = QuotationDto.builder()
                .date(post.getPostCreatedAt())
                .userNickname(user.getUserNickname())
                .userPhoto(user.getUserPhoto())
                .content(post.getPostContent())
                .build();

        return quotationDto;
    }

    // 글 삭제
    public Long deletePost(Long postId) {
        postRepository.deleteById(postId);

        return postId;
    }

    // 글 좋아요
    @Transactional
    public boolean likePost(Long userId, Long postId) {
        String likeKey = "post: " + postId + ":likes";
        String userKey = "user: " + userId + ":liked";

        if (!redisTemplate.opsForSet().isMember(userKey, String.valueOf(postId))) {
            redisTemplate.opsForValue().increment(likeKey);
            redisTemplate.opsForSet().add(userKey, String.valueOf(postId));
            return true;
        } else{
            redisTemplate.opsForValue().decrement(likeKey);
            redisTemplate.opsForSet().remove(userKey, String.valueOf(postId));
            return false;
        }
    }

    // 글 싫어요
    public boolean dislikePost(Long userId, Long postId) {
        String dislikeKey = "post: " + postId + ":dislikes";
        String userKey = "user: " + userId + ":disliked";

        if (!redisTemplate.opsForSet().isMember(userKey, String.valueOf(postId))) {
            redisTemplate.opsForValue().increment(dislikeKey);
            redisTemplate.opsForSet().add(userKey, String.valueOf(postId));
            return true;
        } else {
            redisTemplate.opsForValue().decrement(dislikeKey);
            redisTemplate.opsForSet().remove(userKey, String.valueOf(postId));
            return false;
        }
    }

    // 토론글 북마크
    public boolean bookmark(Long userId, Long postId) {
        User user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();

        if(!postBookmarkRepository.existsByUserAndPost(user, post)){
            PostBookmark postBookmark = PostBookmark.builder()
                    .user(user)
                    .post(post)
                    .build();
            postBookmarkRepository.save(postBookmark);
            return true;
        } else {
            deleteBookmark(userId, postId);
            return false;
        }
    }

    // 북마크 삭제
    public void deleteBookmark(Long userId, Long postId) {
        User user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();

        postBookmarkRepository.deleteByUserAndPost(user, post);
    }

    public Integer getLikeCount(Long postId) {
        String likeKey = "post: " + postId + ":likes";
        Object likeCount = redisTemplate.opsForValue().get(likeKey);

        if(likeCount != null) {
            return Integer.parseInt(likeCount.toString());
        } else {
            return 0;
        }
    }

    public Integer getDislikeCount(Long postId) {
        String dislikeKey = "post: " + postId + ":dislikes";
        Object dislikeCount = redisTemplate.opsForValue().get(dislikeKey);

        if(dislikeCount != null) {
            return Integer.parseInt(dislikeCount.toString());
        } else {
            return 0;
        }
    }

}

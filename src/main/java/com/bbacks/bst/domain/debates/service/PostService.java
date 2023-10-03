package com.bbacks.bst.domain.debates.service;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.debates.domain.Debate;
import com.bbacks.bst.domain.debates.domain.Post;
import com.bbacks.bst.domain.debates.domain.PostBookmark;
import com.bbacks.bst.domain.debates.dto.*;
import com.bbacks.bst.domain.debates.repository.DebateRepository;
import com.bbacks.bst.domain.debates.repository.PostBookmarkRepository;
import com.bbacks.bst.domain.debates.repository.PostRepository;
import com.bbacks.bst.domain.user.repository.UserRepository;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.global.exception.PostNotFoundException;
import com.bbacks.bst.global.filtering.BadWordsFiltering;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
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
    public void createPost(CreatePostRequest createPostRequest, Long debateId, Long userId) {
        Debate debate = debateRepository.getReferenceById(debateId);
        User user = userRepository.getReferenceById(userId);

        BadWordsFiltering badWordsFiltering = new BadWordsFiltering();
        String text = createPostRequest.getPostContent();
        String filteredContent = badWordsFiltering.change(text);

        Post post = Post.builder()
                .debate(debate)
                .user(user)
                .postContent(filteredContent)
                .postPhoto(createPostRequest.getPostPhoto())
                .postQuotationId(createPostRequest.getPostQuotationId())
                .postIsPro(createPostRequest.getPostIsPro())
                .build();

        postRepository.save(post);
    }

    // 글 상세 페이지
    public ReadPostResponse readPost(Long postId) {
        Post postEntity = postRepository.findPostDetailByPostId(postId)
                .orElseThrow(() -> new PostNotFoundException());

        Debate debateEntity = postEntity.getDebate();
        Book bookEntity = debateEntity.getBook();
        User userEntity = postEntity.getUser();

        List<Post> quotationPosts = postRepository.findByPostQuotationId(postId);

        List<QuotationDto> quotationDtos = quotationPosts.stream()
                .map(p -> readQuotation(p))
                .collect(Collectors.toList());

        DebateInfoDto debateInfoDto = DebateInfoDto.builder()
                .bookTitle(bookEntity.getBookTitle())
                .bookAuthor(bookEntity.getBookAuthor())
                .topic(debateEntity.getDebateTopic())
                .build();

        PostDto postDto = PostDto.builder()
                .date(postEntity.getPostCreatedAt())
                .userNickname(userEntity.getUserNickname())
                .userPhoto(userEntity.getUserPhoto())
                .content(postEntity.getPostContent())
                .quotedPostId(postEntity.getPostQuotationId())
                .like(getLikeCount(postId))
                .dislike(getDislikeCount(postId))
                .isPro(postEntity.getPostIsPro())
                .build();

        ReadPostResponse readPostResponse = ReadPostResponse.builder()
                .debateInfoDto(debateInfoDto)
                .postDto(postDto)
                .quotationDtos(quotationDtos)
                .build();

        return readPostResponse;
    }
    public QuotationDto readQuotation(Post post) {

        QuotationDto quotationDto = QuotationDto.builder()
                .date(post.getPostCreatedAt())
                .userNickname(post.getUser().getUserNickname())
                .userPhoto(post.getUser().getUserPhoto())
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

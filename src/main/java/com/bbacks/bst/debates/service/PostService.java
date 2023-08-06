package com.bbacks.bst.debates.service;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.debates.domain.Debate;
import com.bbacks.bst.debates.domain.Post;
import com.bbacks.bst.debates.dto.*;
import com.bbacks.bst.debates.repository.DebateRepository;
import com.bbacks.bst.debates.repository.PostRepository;
import com.bbacks.bst.user.domain.User;
import com.bbacks.bst.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    // 글 작성
    @Transactional
    public Long createPost(CreatePostRequest createPostRequest) {
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

        return post.getPostId();
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
                .like(post.getPostLike())
                .dislike(post.getPostDislike())
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
}

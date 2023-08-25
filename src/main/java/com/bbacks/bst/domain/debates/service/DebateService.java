package com.bbacks.bst.domain.debates.service;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.categories.domain.Category;
import com.bbacks.bst.domain.debates.domain.Debate;
import com.bbacks.bst.domain.debates.domain.Post;
import com.bbacks.bst.domain.debates.domain.UserDebate;
import com.bbacks.bst.domain.debates.dto.*;
import com.bbacks.bst.domain.debates.repository.PostRepository;
import com.bbacks.bst.domain.debates.repository.TempBookRepository;
import com.bbacks.bst.domain.debates.repository.DebateRepository;
import com.bbacks.bst.domain.debates.repository.UserDebateRepository;
import com.bbacks.bst.domain.user.repository.UserRepository;
import com.bbacks.bst.domain.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bbacks.bst.domain.debates.domain.QDebate.debate;
import static com.bbacks.bst.domain.debates.domain.QPost.post;

@Service
@RequiredArgsConstructor
public class DebateService {

    private final UserDebateRepository userDebateRepository;
    private final UserRepository userRepository;
    private final TempBookRepository tempBookRepository;
    private final DebateRepository debateRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    private final JPAQueryFactory queryFactory;

    // 내가 참여한 토론방
    public List<MyDebateResponse> myDebate(Long userId) {

        User user = userRepository.findById(userId).get();
        List<MyDebateResponse> myDebateResponses = new ArrayList<>();
        List<UserDebate> userDebates = userDebateRepository.findByUser(user);

        if(!userDebates.isEmpty()) {
            List<Debate> debates = userDebates.stream().map(UserDebate::getDebate).collect(Collectors.toList());
            for(Debate d:debates) {
                Book book = d.getBook();
                Category category = book.getBookCategory();

                MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                        .bookTitle(book.getBookTitle())
                        .bookAuthor(book.getBookAuthor())
                        .debateTopic(d.getDebateTopic())
                        .debateType(d.getDebateType())
                        .categoryName(category.getCategoryName())
                        .build();

                myDebateResponses.add(myDebateResponse);
            }
        }

        return myDebateResponses;
    }

    // 토론방 개설
    public Long createDebate(CreateDebateRequest createDebateRequest) {
        Book book = tempBookRepository.findById(createDebateRequest.getBookId()).get();
        Debate debate = Debate.builder()
                .book(book)
                .debateTopic(createDebateRequest.getDebateTopic())
                .debateType(createDebateRequest.getDebateType())
                .debateDescription(createDebateRequest.getDebateDescription())
                .debatePassword(createDebateRequest.getDebatePassword())
                .debateParticipants(1)
                .build();
        debateRepository.save(debate);

        return debate.getDebateId();
    }

    // 토론방 개요 페이지
    // 주제, 개요, 타입, 참여자 수, 개설 날짜, 비밀번호 유무
    public DebateOutlineResponse debateOutline (Long debateId) {
        Debate debate = debateRepository.findById(debateId).get();
        int isPrivate = (debate.getDebatePassword() == null) ? 0 : 1;

        DebateOutlineResponse debateOutlineResponse = DebateOutlineResponse.builder()
                .debateTopic(debate.getDebateTopic())
                .debateType(debate.getDebateType())
                .debateParticipants(debate.getDebateParticipants())
                .debateCreatedAt(debate.getDebateCreatedAt())
                .isPrivate(isPrivate)
                .build();

        return debateOutlineResponse;
    }

    // 토론방 참여하기
    @Transactional
    public int join(Long debateId, Long userId) {
        // 내가 참여한 토론에 추가
        User user = userRepository.findById(userId).get();
        Debate debate = debateRepository.findById(debateId).get();
        UserDebate userDebate = UserDebate.builder()
                .user(user)
                .debate(debate)
                .build();

        userDebateRepository.save(userDebate);

        // debateParticipants ++1
        return debateRepository.incrementCountByDebateId(debateId);
    }

    // 토론방 피드
    public List<PostDto> debateFeed(Long debateId) {
        Debate debate = debateRepository.findById(debateId).get();
        List<Post> postList = postRepository.findByDebate(debate);

        List<PostDto> posts = new ArrayList<>();

        if(!postList.isEmpty()){
            for(Post p:postList) {
                User user = p.getUser();
                PostDto postDto = PostDto.builder()
                        .date(p.getPostCreatedAt())
                        .userNickname(user.getUserNickname())
                        .userPhoto(user.getUserPhoto())
                        .content(p.getPostContent())
                        .like(postService.getLikeCount(p.getPostId()))
                        .dislike(postService.getDislikeCount(p.getPostId()))
                        .isPro(p.getPostIsPro())
                        .build();

                posts.add(postDto);
            }
        }

        return posts;
    }

    @Transactional(readOnly = true)
    public List<DebateInBookDetailResponse> getBookDetailDebateNoOffset(Long bookId, Long debateId){
        BooleanBuilder dynamicLtId = new BooleanBuilder();
        if(debateId != null){
            dynamicLtId.and(debate.debateId.lt(debateId));
        }

        return queryFactory.select(Projections.constructor(DebateInBookDetailResponse.class,
                debate.debateTopic,
                debate.debateId,
                debate.debateType,
                debate.posts.size().as("debatePostCount")))
                .from(debate)
                .leftJoin(debate.posts, post)
                .where(dynamicLtId
                        .and(debate.book.bookId.eq(bookId)))
                .groupBy(debate.debateId)
                .orderBy(debate.debateId.desc())
                .limit(3)
                .fetch();

    }



}

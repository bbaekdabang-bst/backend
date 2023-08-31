package com.bbacks.bst.domain.debates.service;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.books.repository.BookRepository;
import com.bbacks.bst.domain.categories.domain.Category;
import com.bbacks.bst.domain.debates.domain.Debate;
import com.bbacks.bst.domain.debates.domain.Post;
import com.bbacks.bst.domain.debates.domain.UserDebate;
import com.bbacks.bst.domain.debates.dto.CreateDebateRequest;
import com.bbacks.bst.domain.debates.dto.DebateOutlineResponse;
import com.bbacks.bst.domain.debates.dto.MyDebateResponse;
import com.bbacks.bst.domain.debates.dto.PostDto;
import com.bbacks.bst.domain.debates.repository.PostRepository;
import com.bbacks.bst.domain.debates.repository.DebateRepository;
import com.bbacks.bst.domain.debates.repository.UserDebateRepository;
import com.bbacks.bst.domain.user.repository.UserRepository;
import com.bbacks.bst.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebateService {

    private final UserDebateRepository userDebateRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final DebateRepository debateRepository;
    private final PostRepository postRepository;
    private final PostService postService;

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
        Book book = bookRepository.findById(createDebateRequest.getBookId()).get();
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
    // 주제, 타입, 책 제목, 저자, 개요, 참여자 수, 개설 날짜, 비밀번호 유무
    public DebateOutlineResponse debateOutline (Long debateId) {
        Debate debate = debateRepository.findById(debateId).get();
        Book book = debate.getBook();
        Category category = book.getBookCategory();
        int isPrivate = (debate.getDebatePassword() == null) ? 0 : 1;

        DebateOutlineResponse debateOutlineResponse = DebateOutlineResponse.builder()
                .debateTopic(debate.getDebateTopic())
                .debateType(debate.getDebateType())
                .bookTitle(book.getBookTitle())
                .bookAuthor(book.getBookAuthor())
                .cateogory(category.getCategoryName())
                .debateDescription(debate.getDebateDescription())
                .debateParticipants(debate.getDebateParticipants())
                .debateCreatedAt(debate.getDebateCreatedAt())
                .isPrivate(isPrivate)
                .build();

        return debateOutlineResponse;
    }

    public int isPrivate(Long debateId) {
        Debate debate = debateRepository.findById(debateId).get();
        int isPrivate = (debate.getDebatePassword() == null) ? 0 : 1;

        return isPrivate;
    }

    public int isValidPassword(Long debateId, String password) {
        Debate debate = debateRepository.findById(debateId).get();
        int isValid = (debate.getDebatePassword().equals(password)) ? 1 : 0;
        System.out.println(debate.getDebatePassword() + " / " + password);

        return isValid;
    }

    // 토론방 참여하기
    @Transactional
    public int join(Long debateId, Long userId, String password) {

        // 비밀방이면서 비밀번호가 틀렸거나 null인 경우
        if (isPrivate(debateId)==1 && (isValidPassword(debateId, password)==0 || password == null)) {
            return 0;
        }
        // 내가 참여한 토론에 추가
        User user = userRepository.findById(userId).get();
        Debate debate = debateRepository.findById(debateId).get();

        UserDebate userDebate = UserDebate.builder()
                .user(user)
                .debate(debate)
                .build();

        userDebateRepository.save(userDebate);
        // debateParticipants ++1
        debateRepository.incrementCountByDebateId(debateId);

        return 1;
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
                        .quotedPostId(p.getPostQuotationId())
                        .like(postService.getLikeCount(p.getPostId()))
                        .dislike(postService.getDislikeCount(p.getPostId()))
                        .isPro(p.getPostIsPro())
                        .build();

                posts.add(postDto);
            }
        }

        return posts;
    }



}

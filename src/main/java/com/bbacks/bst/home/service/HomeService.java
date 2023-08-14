package com.bbacks.bst.home.service;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.categories.domain.Category;
import com.bbacks.bst.debates.domain.Debate;
import com.bbacks.bst.debates.domain.UserDebate;
import com.bbacks.bst.debates.dto.MyDebateResponse;
import com.bbacks.bst.debates.repository.DebateRepository;
import com.bbacks.bst.home.dto.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final DebateRepository debateRepository;

    // 메인 화면
    public HomeResponse home() {
        List<Debate> bestDebates = debateRepository.findTop10ByOrderByDebateParticipants();
        List<Debate> newDebates = debateRepository.findTop10ByOrderByDebateCreatedAtDesc();

        List<MyDebateResponse> bestDebateList = new ArrayList<>();
        List<MyDebateResponse> newDebateList = new ArrayList<>();

        for(Debate d:bestDebates) {
            Book book = d.getBookId();
            Category category = book.getBookCategory();

            MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                    .bookTitle(book.getBookTitle())
                    .bookAuthor(book.getBookAuthor())
                    .debateTopic(d.getDebateTopic())
                    .debateType(d.getDebateType())
                    .categoryName(category.getCategoryName())
                    .build();

            bestDebateList.add(myDebateResponse);
        }

        for(Debate d:newDebates) {
            Book book = d.getBookId();
            Category category = book.getBookCategory();

            MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                    .bookTitle(book.getBookTitle())
                    .bookAuthor(book.getBookAuthor())
                    .debateTopic(d.getDebateTopic())
                    .debateType(d.getDebateType())
                    .categoryName(category.getCategoryName())
                    .build();

            newDebateList.add(myDebateResponse);
        }

        HomeResponse homeResponse = HomeResponse.builder()
                .bestDebates(bestDebateList)
                .newDebates(newDebateList)
                .build();
        return homeResponse;
    }
}
package com.bbacks.bst.home.service;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.categories.domain.Category;
import com.bbacks.bst.debates.domain.Debate;
import com.bbacks.bst.debates.domain.UserDebate;
import com.bbacks.bst.debates.dto.MyDebateResponse;
import com.bbacks.bst.debates.repository.DebateRepository;
import com.bbacks.bst.debates.repository.TempBookRepository;
import com.bbacks.bst.home.dto.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final DebateRepository debateRepository;
    private final TempBookRepository tempBookRepository;

    // 메인 화면
    public HomeResponse home() {
        List<MyDebateResponse> bestDebateList = bestDebates();
        List<MyDebateResponse> newDebateList = newDebates();

        HomeResponse homeResponse = HomeResponse.builder()
                .bestDebates(bestDebateList)
                .newDebates(newDebateList)
                .build();
        return homeResponse;
    }

    // 도서명, 작가명으로 토론방 검색
    public List<MyDebateResponse> searchDebates(String keyword) {
        List<MyDebateResponse> results = new ArrayList<>();
        List<Book> bookResults = tempBookRepository.findByBookTitleContainingOrBookAuthorContaining(keyword, keyword);

        for(Book b:bookResults) {
            List<Debate> debates = debateRepository.findByBookId(b);
            Category category = b.getBookCategory();

            for(Debate d:debates) {
                MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                        .bookTitle(b.getBookTitle())
                        .bookAuthor(b.getBookAuthor())
                        .debateTopic(d.getDebateTopic())
                        .debateType(d.getDebateType())
                        .categoryName(category.getCategoryName())
                        .build();
                results.add(myDebateResponse);
            }
        }
        return results;
    }

    // 인기 토론방
    public List<MyDebateResponse> bestDebates() {
        List<Debate> bestDebates = debateRepository.findTop10ByOrderByDebateParticipants();
        List<MyDebateResponse> bestDebateList = new ArrayList<>();

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
        return bestDebateList;
    }

    // 최신 토론방
    public List<MyDebateResponse> newDebates() {
        List<Debate> newDebates = debateRepository.findTop10ByOrderByDebateCreatedAtDesc();
        List<MyDebateResponse> newDebateList = new ArrayList<>();

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
        return newDebateList;
    }
}
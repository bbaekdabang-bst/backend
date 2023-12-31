package com.bbacks.bst.domain.home.service;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.books.repository.BookRepository;
import com.bbacks.bst.domain.categories.domain.Category;
import com.bbacks.bst.domain.categories.repository.CategoryRepository;
import com.bbacks.bst.domain.debates.domain.Debate;
import com.bbacks.bst.domain.debates.dto.MyDebateResponse;
import com.bbacks.bst.domain.debates.repository.DebateRepository;
import com.bbacks.bst.domain.home.dto.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final DebateRepository debateRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

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
        List<Book> bookResults = bookRepository.findByBookTitleContainingOrBookAuthorContaining(keyword, keyword);

        for(Book b:bookResults) {
            List<Debate> debates = debateRepository.findByBook(b);
            Category category = b.getBookCategory();

            for(Debate d:debates) {
                MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                        .bookTitle(b.getBookTitle())
                        .bookAuthor(b.getBookAuthor())
                        .debateId(d.getDebateId())
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
        List<Debate> bestDebates = debateRepository.findTop10ByOrderByDebateParticipantsDesc();
        List<MyDebateResponse> bestDebateList = new ArrayList<>();

        for(Debate d:bestDebates) {
            Book book = d.getBook();
            Category category = book.getBookCategory();

            MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                    .bookTitle(book.getBookTitle())
                    .bookAuthor(book.getBookAuthor())
                    .debateId(d.getDebateId())
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
            Book book = d.getBook();
            Category category = book.getBookCategory();

            MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                    .bookTitle(book.getBookTitle())
                    .bookAuthor(book.getBookAuthor())
                    .debateId(d.getDebateId())
                    .debateTopic(d.getDebateTopic())
                    .debateType(d.getDebateType())
                    .categoryName(category.getCategoryName())
                    .build();

            newDebateList.add(myDebateResponse);
        }
        return newDebateList;
    }

    // 책 카테고리 별 토론방
    public List<MyDebateResponse> categoryDebates(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        List<Book> books = bookRepository.findByBookCategory(category);
        List<MyDebateResponse> categoryDebates = new ArrayList<>();
        for(Book b:books) {
            List<Debate> debates = debateRepository.findByBook(b);

            for(Debate d:debates) {
                MyDebateResponse myDebateResponse = MyDebateResponse.builder()
                        .bookTitle(b.getBookTitle())
                        .bookAuthor(b.getBookAuthor())
                        .debateId(d.getDebateId())
                        .debateTopic(d.getDebateTopic())
                        .debateType(d.getDebateType())
                        .categoryName(category.getCategoryName())
                        .build();

                categoryDebates.add(myDebateResponse);
            }
        }
        return categoryDebates;
    }
}
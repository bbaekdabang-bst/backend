package com.bbacks.bst.books.dto;

import com.bbacks.bst.books.repository.BookImgAndId;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMainResponse {

    private List<BookImgAndId> best10Books;
    private List<BookImgAndId> recent10Books;

    /**
     * method 이름에 of, from 많이 쓰던데 각 무슨 의미?
     */
    public static BookMainResponse of(List<BookImgAndId> best10BookImgAndId, List<BookImgAndId> recent10BookImgAndId){
        return BookMainResponse.builder()
                .best10Books(best10BookImgAndId)
                .recent10Books(recent10BookImgAndId)
                .build();
    }

}

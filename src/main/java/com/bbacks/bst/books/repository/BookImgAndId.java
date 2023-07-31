package com.bbacks.bst.books.repository;

import com.bbacks.bst.categories.domain.Category;

public interface BookImgAndId {
    /**
     * 의문. 만약 img, id에 추가적으로 더 가져올 데이터가 생긴다면 이 인터페이스를 사용하는 모든 곳을 수정해야 한다... 좋은 설계 방법 없을까?
     */
    Long getBookId();
    String getBookImg();
//    MyCategory getBookCategory();
//
//    interface MyCategory {
//        String getCategoryName();
//    }

}

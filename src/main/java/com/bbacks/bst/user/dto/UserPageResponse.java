package com.bbacks.bst.user.dto;

import com.bbacks.bst.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPageResponse {

    private String userNickname;
    private String userImg;
    private Integer userLevel;

    public static UserPageResponse of(User user) {
        return UserPageResponse.builder()
                .userNickname(user.getUserNickname())
                .userImg(user.getUserPhoto())
                .userLevel(user.getUserLevel())
                .build();
    }
}

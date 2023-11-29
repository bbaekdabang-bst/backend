package com.bbacks.bst.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfo {

    private String userNickname;

    private String userPhoto;
}

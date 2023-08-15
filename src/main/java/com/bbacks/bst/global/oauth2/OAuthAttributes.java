package com.bbacks.bst.global.oauth2;

import com.bbacks.bst.global.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.bbacks.bst.global.oauth2.userinfo.NaverOAuth2UserInfo;
import com.bbacks.bst.global.oauth2.userinfo.OAuth2UserInfo;
import com.bbacks.bst.domain.user.domain.PlatformType;
import com.bbacks.bst.domain.user.domain.Role;
import com.bbacks.bst.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(이름, 이메일 등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }


    //플랫폼 타입에 맞는 객체 반환
    public static OAuthAttributes of(PlatformType platformType,
                                     String userNameAttributeName, Map<String, Object> attributes) {
        if (platformType == PlatformType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }

        return ofKakao(userNameAttributeName, attributes);
    }

    //카카오
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    //네이버
    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체 생성, 유저 정보들이 담긴 OAuth2UserInfo가 플랫폼 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값)을 가져와서 내 DB에 들어갈 User build
     * role은 GUEST로 설정
     **/
    public User toEntity(PlatformType platformType, OAuth2UserInfo oAuth2UserInfo){
        return User.builder()
                .userPlatform(platformType)
                .userSocialId(oAuth2UserInfo.getId())
                .userNickname("닉네임"+UUID.randomUUID().toString().substring(0,5)) //닉네임 랜덤 생성
                .userLevel(1)
                .userRole(Role.USER)
                .build();
    }

}

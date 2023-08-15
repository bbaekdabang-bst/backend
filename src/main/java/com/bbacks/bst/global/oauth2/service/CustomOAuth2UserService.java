package com.bbacks.bst.global.oauth2.service;

import com.bbacks.bst.global.oauth2.CustomOAuth2User;
import com.bbacks.bst.global.oauth2.OAuthAttributes;
import com.bbacks.bst.domain.user.domain.PlatformType;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j //로깅에 사용됨
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";


    /**
     * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
     * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
     * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
     * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
     **/
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        PlatformType platformType = getPlatformType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); //소셜 로그인 API가 제공하는 userInfo의 Json 값(유저 정보)

        log.info("platformType:{}", platformType);
        log.info("userNameAttributeName:{}", userNameAttributeName);
        log.info("attributes:{}", attributes);

        //소셜 플랫폼 타입에 따라 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(platformType, userNameAttributeName, attributes);

        User createdUser = getUser(extractAttributes, platformType);

        //CustomOAuth2User 객체 생성하여 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getUserRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getUserRole(),
                createdUser.getUserPlatform()
        );
    }


    /** 소셜 플랫폼을 반환하는 메소드 **/
    private PlatformType getPlatformType(String registrationId){
        if(NAVER.equals(registrationId)) {
            return PlatformType.NAVER;
        }
        return PlatformType.KAKAO;
    }


    private User getUser(OAuthAttributes attributes, PlatformType platformType){

        User findUser = userRepository.findByUserPlatformAndUserSocialId(platformType,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if(findUser == null){
            return saveUser(attributes, platformType);
        }
        return findUser;
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
     * 생성된 User 객체를 DB에 저장 : PlatformType, socialId, nickname, level 값만 있는 상태
     */
    private User saveUser(OAuthAttributes attributes, PlatformType platformType){
        User createdUser = attributes.toEntity(platformType, attributes.getOauth2UserInfo());
        return userRepository.save(createdUser);
    }
}

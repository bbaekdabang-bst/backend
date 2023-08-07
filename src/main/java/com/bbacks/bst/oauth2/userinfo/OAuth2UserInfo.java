package com.bbacks.bst.oauth2.userinfo;

import java.util.Map;


public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜로그인 시 각 식별자 값(카카오 - id, 네이버 - id)
}

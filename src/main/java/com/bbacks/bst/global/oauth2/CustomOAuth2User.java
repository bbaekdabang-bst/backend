package com.bbacks.bst.global.oauth2;

import com.bbacks.bst.domain.user.domain.PlatformType;
import com.bbacks.bst.domain.user.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    //private String email;
    private Role role;
    private PlatformType platformType;
    private String userNameAttributeName;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            Role role,
                            PlatformType platformType) {
        super(authorities, attributes, nameAttributeKey);
        this.role = role;
        this.platformType = platformType;
        this.userNameAttributeName = nameAttributeKey;
    }


}

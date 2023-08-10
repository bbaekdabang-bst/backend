package com.bbacks.bst.user.domain;

//import com.bbacks.bst.books.domain.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id @GeneratedValue
    @Column(name = "u_id")
    private Long userId;

    @Column(name = "u_nickname", unique = true, length = 20)
    private String userNickname;

    @Column(name = "u_platform")
    private PlatformType userPlatform; //Naver, Kakao

    @Column(name = "u_social_id")
    private String userSocialId; //소셜로그인 식별자 -> jwt 발급 용도

    @Column(name = "u_refresh_token")
    private String userToken; //Refresh Token

    @Column(name = "u_photo")
    private String userPhoto;

    @Column(name = "u_level")
    private Integer userLevel;

    @Column(name = "u_role")
    private Role userRole;

    /*
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private List<Review> reviews;
*/
    public void updateRefreshToken(String userToken) {
        this.userToken = userToken;
    }
}

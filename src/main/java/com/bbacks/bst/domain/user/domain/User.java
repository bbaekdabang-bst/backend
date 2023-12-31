package com.bbacks.bst.domain.user.domain;

import com.bbacks.bst.domain.reviews.domain.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id")
    private Long userId;

    @Column(name = "u_nickname", unique = true, length = 20)
    private String userNickname;

    @Column(name = "u_platform")
    @Enumerated(EnumType.STRING)
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
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("reviewId DESC")
    private List<Review> reviews;

    public void updateRefreshToken(String userToken) {
        this.userToken = userToken;
    }

    public void updateNickname(String userNickname){ this.userNickname = userNickname; }

    public void updatePhoto(String userPhoto){ this.userPhoto = userPhoto; }
}

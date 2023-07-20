package com.bbacks.bst.user.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id @GeneratedValue
    @Column(name = "u_id")
    private int userId;

    @Column(name = "u_nickname", unique = true, length = 20)
    private String userNickname;

    @Column(name = "u_social_id")
    private String userSocialId;

    @Column(name = "u_photo")
    private String userPhoto;

    @Column(name = "u_platform")
    private String userPlatform;

    @Column(name = "u_level")
    private int userLevel;

    @Column(name = "u_refresh_token")
    private String userToken;

}

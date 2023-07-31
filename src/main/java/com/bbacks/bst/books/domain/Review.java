package com.bbacks.bst.books.domain;

import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
public class Review {
    @Id @GeneratedValue
    @Column(name = "r_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_id")
    private Book book;

    @Column(name = "r_title")
    private String reviewTitle;

    @Column(name = "r_picture")
    private String reviewImg;

    @Column(name = "r_content")
    private String reviewContent;

    @Column(name = "r_spoiler", columnDefinition = "TINYINT(1)")
    private Boolean reviewSpoiler;

    @Column(name = "r_private", columnDefinition = "TINYINT(1)")
    private Boolean reviewPrivate;

    @Column(name = "r_created_date")
    private String reviewCreatedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review")
    private List<ReviewComment> reviewComment = new ArrayList<>();


}

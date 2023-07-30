package com.bbacks.bst.books.domain;

import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

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
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_id")
    private Book bookId;

    @Column(name = "r_title")
    private String reviewTitle;

    @Column(name = "r_picture")
    private String reviewImg;

    @Column(name = "r_text")
    private String reviewText;

    @Column(name = "r_spoiler", columnDefinition = "TINYINT(1)")
    private Boolean reviewSpoiler;

    @Column(name = "r_private", columnDefinition = "TINYINT(1)")
    private Boolean reviewPrivate;

    @Column(name = "r_created_date")
    private String reviewCreatedDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_id")
    private List<ReviewComment> reviewComment;


}

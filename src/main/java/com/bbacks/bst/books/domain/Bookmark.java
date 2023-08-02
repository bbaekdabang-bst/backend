package com.bbacks.bst.books.domain;

import com.bbacks.bst.reviews.domain.Review;
import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "bookmark")
@Getter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bm_id")
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_id")
    private Review review;

    public Bookmark() {

    }

    public Bookmark(User user, Review review) {
        this.user = user;
        this.review = review;
    }

}

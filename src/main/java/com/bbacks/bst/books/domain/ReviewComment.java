package com.bbacks.bst.books.domain;

import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "review_comment")
@Getter
public class ReviewComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    @Column(name = "comment_text")
    private String commentText;


}
